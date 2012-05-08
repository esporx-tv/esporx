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

import tv.esporx.dao.PersistenceCapableCast;
import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;
import tv.esporx.domain.front.CastSlot;
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
	private PersistenceCapableCast castDao;
	@Autowired
	private final CastBroadcastDateComparator broadcastDateComparator = new CastBroadcastDateComparator();
	@Autowired
	private final CastByEventSlotIndexer castByEventSlotIndexer = new CastByEventSlotIndexer();
	@Autowired
	private final CastToEventConverterFunction castToEventConverter = new CastToEventConverterFunction();
	@Autowired
	private final CastToCastSlotConverterFunction castToCastSlotConverter = new CastToCastSlotConverterFunction();
	private TimelineDimensions dimensions = new TimelineDimensions();
	private Set<Cast> previouslyMatchedCasts;

	public Timeline buildTimeline(final DateTime startDate, final TimelineDimensions dimensions) {
		this.dimensions = dimensions;
		return buildTimeline(startDate);
	}

	public Timeline buildTimeline(final DateTime startDate) {
		Timeline result = new Timeline();
		DateTime maxDate = lastPossibleDate(startDate);
		Set<Cast> allCasts = Sets.filter(retrieveCastsBetween(startDate, maxDate), new CastWithNotNullEventPredicate());
		if (!allCasts.isEmpty()) {
			for (DateTime currentColumn = earliestBroadcastDateOf(allCasts); currentColumn.isBefore(lastBroadcastDateOf(allCasts).plusSeconds(1)); currentColumn = nextColumn(currentColumn)) {
				result.add(buildColumn(allCasts, currentColumn));
			}
		}
		return result;
	}

	private DateTime lastPossibleDate(final DateTime startDate) {
		return dimensions.getMaxWidth().plus(startDate).withTimeAtStartOfDay().minusSeconds(1);
	}

	private Set<Cast> retrieveCastsBetween(final DateTime startDate, final DateTime maxDate) {
		return new HashSet<Cast>(castDao.findTimeLine(startDate, maxDate));
	}

	private DateTime earliestBroadcastDateOf(final Set<Cast> allCasts) {
		DateTime broadcastDate = new DateTime(Ordering.from(broadcastDateComparator).min(allCasts).getBroadcastDate());
		return broadcastDate.minusMinutes(broadcastDate.getMinuteOfHour() % (dimensions.getRowInterval().asMilliseconds() / 60000));
	}

	private DateTime lastBroadcastDateOf(final Set<Cast> allCasts) {
		DateTime broadcastDate = new DateTime(Ordering.from(broadcastDateComparator).max(allCasts).getBroadcastDate());
		return broadcastDate.minusMinutes(broadcastDate.getMinuteOfHour() % (dimensions.getRowInterval().asMilliseconds() / 60000));
	}

	private DateTime nextColumn(final DateTime currentColumn) {
		return dimensions.getColumnInterval().plus(currentColumn.withTimeAtStartOfDay());
	}

	private TimelineColumn buildColumn(final Set<Cast> allCasts, final DateTime currentColumn) {
		reset();
		TimelineColumn timelineColumn = new TimelineColumn(currentColumn);
		for (DateTime currentSlot = currentColumn; currentSlot.isBefore(columnBottom(currentColumn)); currentSlot = nextRow(currentSlot)) {
			TimelineRow row = buildRow(allCasts, currentSlot);
			if (row != null) {
				timelineColumn.add(row);
			}
		}
		return timelineColumn;
	}

	private void reset() {
		previouslyMatchedCasts = new HashSet<Cast>();
	}


	private DateTime columnBottom(final DateTime currentColumn) {
		return dimensions.getMaxColumnHeight().plus(currentColumn.withTimeAtStartOfDay()).minusSeconds(1);
	}

	private DateTime nextRow(final DateTime currentSlotStart) {
		return dimensions.getRowInterval().plus(currentSlotStart);
	}

	private TimelineRow buildRow(final Set<Cast> allCasts, final DateTime currentSlotStart) {
		DateTime slotStart = currentSlotStart;
		DateTime slotEnd = nextPossibleSlot(currentSlotStart).minusSeconds(1);
		Multimap<EventSlot, CastSlot> matchingSlots = buildMatchingSlots(allCasts, slotStart, slotEnd);
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

	private Multimap<EventSlot, CastSlot> buildMatchingSlots(final Set<Cast> allCasts, final DateTime slotStart, final DateTime slotEnd) {
		Set<Cast> matchingCasts = findAndTrackMatchingCasts(allCasts, slotStart, slotEnd);
		Multimap<EventSlot, Cast> indexedCasts = index(matchingCasts, castByEventSlotIndexer);
		Multimap<EventSlot, CastSlot> result = transformValues(indexedCasts, castToCastSlotConverter);
		return result;
	}

	private Set<Cast> findAndTrackMatchingCasts(final Set<Cast> allCasts, final DateTime slotStart, final DateTime slotEnd) {
		Set<Cast> onlyNewMembers = findMatchingCasts(allCasts, slotStart, slotEnd);
		trackMatchingSlots(onlyNewMembers);
		return onlyNewMembers;
	}

	private Set<Cast> findMatchingCasts(final Set<Cast> allCasts, final DateTime slotStart, final DateTime slotEnd) {
		Set<Cast> onlyNewMembers = newHashSet(difference(findNaturalCastMatches(allCasts, slotStart), previouslyMatchedCasts));
		Set<Cast> magnetizedMembers = magnetizedCastMatches(allCasts, slotStart, slotEnd, onlyNewMembers);
		onlyNewMembers.addAll(magnetizedMembers);
		return onlyNewMembers;
	}

	private Set<Cast> findNaturalCastMatches(final Set<Cast> allCasts, final DateTime slotStart) {
		return filter(allCasts, new CastInRowPredicate(slotStart, nextRow(slotStart).minusSeconds(1)));
	}

	private Set<Cast> magnetizedCastMatches(final Set<Cast> allCasts, final DateTime slotStart, final DateTime slotEnd, final Set<Cast> naturalMembers) {
		Iterable<Event> parentEvents = newHashSet(transform(naturalMembers, castToEventConverter));
		Set<Cast> magnetizedMembers = filter(allCasts, new CastInSlotPredicate(nextRow(slotStart), slotEnd, parentEvents));
		return magnetizedMembers;
	}

	private void trackMatchingSlots(final Set<Cast> onlyNewMembers) {
		previouslyMatchedCasts.addAll(onlyNewMembers);
	}
}
