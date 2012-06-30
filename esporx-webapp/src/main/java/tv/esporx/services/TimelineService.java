package tv.esporx.services;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Multimaps.index;
import static com.google.common.collect.Multimaps.transformValues;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tv.esporx.dao.PersistenceCapableBroadcast;
import tv.esporx.domain.Broadcast;
import tv.esporx.domain.Event;
import tv.esporx.domain.front.BroadcastSlot;
import tv.esporx.domain.front.EventSlot;
import tv.esporx.domain.front.Timeline;
import tv.esporx.domain.front.TimelineColumn;
import tv.esporx.domain.front.TimelineDimensions;
import tv.esporx.domain.front.TimelineRow;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

@Service
public class TimelineService {

	@Autowired
	private PersistenceCapableBroadcast broadcastDao;
	@Autowired
	private final BroadcastDateComparator broadcastDateComparator = new BroadcastDateComparator();
	@Autowired
	private final BroadcastByEventSlotIndexer channelByEventSlotIndexer = new BroadcastByEventSlotIndexer();
	@Autowired
	private final BroadcastToEventConverterFunction channelToEventConverter = new BroadcastToEventConverterFunction();
	@Autowired
	private final BroadcastToBroadcastSlotConverterFunction channelToChannelSlotConverter = new BroadcastToBroadcastSlotConverterFunction();
	private TimelineDimensions dimensions = new TimelineDimensions();
	private Set<Broadcast> previouslyMatchedChannels;

	public Timeline buildTimeline(final DateTime startDate, final TimelineDimensions dimensions) {
		this.dimensions = dimensions;
		return buildTimeline(startDate);
	}

	public Timeline buildTimeline(final DateTime startDate) {
		Timeline result = new Timeline();
		DateTime maxDate = lastPossibleDate(startDate);
		Set<Broadcast> allChannels = Sets.filter(retrieveChannelsBetween(startDate, maxDate), new BroadcastWithNotNullEventPredicate());
		if (!allChannels.isEmpty()) {
			for (DateTime currentColumn = earliestBroadcastDateOf(allChannels); currentColumn.isBefore(lastBroadcastDateOf(allChannels).plusSeconds(1)); currentColumn = nextColumn(currentColumn)) {
				result.add(buildColumn(allChannels, currentColumn));
			}
		}
		return result;
	}

	private DateTime lastPossibleDate(final DateTime startDate) {
		return dimensions.getMaxWidth().plus(startDate).withTimeAtStartOfDay().minusSeconds(1);
	}

	private Set<Broadcast> retrieveChannelsBetween(final DateTime startDate, final DateTime maxDate) {
		return new HashSet<Broadcast>(broadcastDao.findTimeLine(startDate, maxDate));
	}

	private DateTime earliestBroadcastDateOf(final Set<Broadcast> allChannels) {
		DateTime broadcastDate = new DateTime(Ordering.from(broadcastDateComparator).min(allChannels).getBroadcastDate());
		return broadcastDate.minusMinutes(broadcastDate.getMinuteOfHour() % (dimensions.getRowInterval().asMilliseconds() / 60000));
	}

	private DateTime lastBroadcastDateOf(final Set<Broadcast> allChannels) {
		DateTime broadcastDate = new DateTime(Ordering.from(broadcastDateComparator).max(allChannels).getBroadcastDate());
		return broadcastDate.minusMinutes(broadcastDate.getMinuteOfHour() % (dimensions.getRowInterval().asMilliseconds() / 60000));
	}

	private DateTime nextColumn(final DateTime currentColumn) {
		return dimensions.getColumnInterval().plus(currentColumn.withTimeAtStartOfDay());
	}

	private TimelineColumn buildColumn(final Set<Broadcast> allChannels, final DateTime currentColumn) {
		reset();
		TimelineColumn timelineColumn = new TimelineColumn(currentColumn);
		for (DateTime currentSlot = currentColumn; currentSlot.isBefore(columnBottom(currentColumn)); currentSlot = nextRow(currentSlot)) {
			TimelineRow row = buildRow(allChannels, currentSlot);
			if (row != null) {
				timelineColumn.add(row);
			}
		}
		return timelineColumn;
	}

	private void reset() {
		previouslyMatchedChannels = new HashSet<Broadcast>();
	}


	private DateTime columnBottom(final DateTime currentColumn) {
		return dimensions.getMaxColumnHeight().plus(currentColumn.withTimeAtStartOfDay()).minusSeconds(1);
	}

	private DateTime nextRow(final DateTime currentSlotStart) {
		return dimensions.getRowInterval().plus(currentSlotStart);
	}

	private TimelineRow buildRow(final Set<Broadcast> allChannels, final DateTime currentSlotStart) {
		DateTime slotStart = currentSlotStart;
		DateTime slotEnd = nextPossibleSlot(currentSlotStart).minusSeconds(1);
		Multimap<EventSlot, BroadcastSlot> matchingSlots = buildMatchingSlots(allChannels, slotStart, slotEnd);
		if (!matchingSlots.isEmpty()) {
			TimelineRow timelineRow = new TimelineRow(slotStart, slotEnd);
			timelineRow.putAll(matchingSlots);
			return timelineRow;
		}
		return null;
	}

	private DateTime nextPossibleSlot(final DateTime currentSlotStart) {
		return dimensions.getMaxSlotHeight().plus(currentSlotStart);
	}

	private Multimap<EventSlot, BroadcastSlot> buildMatchingSlots(final Set<Broadcast> allChannels, final DateTime slotStart, final DateTime slotEnd) {
		Set<Broadcast> matchingChannels = findAndTrackMatchingChannels(allChannels, slotStart, slotEnd);
		Multimap<EventSlot, Broadcast> indexedChannels = index(matchingChannels, channelByEventSlotIndexer);
		Multimap<EventSlot, BroadcastSlot> result = transformValues(indexedChannels, channelToChannelSlotConverter);
		return result;
	}

	private Set<Broadcast> findAndTrackMatchingChannels(final Set<Broadcast> allChannels, final DateTime slotStart, final DateTime slotEnd) {
		Set<Broadcast> onlyNewMembers = findMatchingChannels(allChannels, slotStart, slotEnd);
		trackMatchingSlots(onlyNewMembers);
		return onlyNewMembers;
	}

	private Set<Broadcast> findMatchingChannels(final Set<Broadcast> allChannels, final DateTime slotStart, final DateTime slotEnd) {
		Set<Broadcast> onlyNewMembers = newHashSet(difference(findNaturalChannelMatches(allChannels, slotStart), previouslyMatchedChannels));
		Set<Broadcast> magnetizedMembers = magnetizedChannelMatches(allChannels, slotStart, slotEnd, onlyNewMembers);
		onlyNewMembers.addAll(magnetizedMembers);
		return onlyNewMembers;
	}

	private Set<Broadcast> findNaturalChannelMatches(final Set<Broadcast> allChannels, final DateTime slotStart) {
		return filter(allChannels, new BroadcastInRowPredicate(slotStart, nextRow(slotStart).minusSeconds(1)));
	}

	private Set<Broadcast> magnetizedChannelMatches(final Set<Broadcast> allChannels, final DateTime slotStart, final DateTime slotEnd, final Set<Broadcast> naturalMembers) {
		Iterable<Event> parentEvents = newHashSet(transform(naturalMembers, channelToEventConverter));
		Set<Broadcast> magnetizedMembers = filter(allChannels, new BroadcastInSlotPredicate(nextRow(slotStart), slotEnd, parentEvents));
		return magnetizedMembers;
	}

	private void trackMatchingSlots(final Set<Broadcast> onlyNewMembers) {
		previouslyMatchedChannels.addAll(onlyNewMembers);
	}
}
