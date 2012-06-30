package tv.esporx.services;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.joda.time.DateTime;

import tv.esporx.domain.Broadcast;
import tv.esporx.domain.Event;

/**
 * Determines if a channel can be "magnetized", i.e. if contained in the slot time
 * window and related to one of the current row events
 */
public class BroadcastInSlotPredicate extends BroadcastInRowPredicate {

	private final List<Event> eligibleEvents;

	public BroadcastInSlotPredicate(final DateTime start, final DateTime end, final Iterable<Event> eligibleEvents) {
		super(start, end);
		this.eligibleEvents = newArrayList(eligibleEvents);
	}

	@Override
	public boolean apply(final Broadcast channel) {
		return eligibleEvents.contains(channel.getEvent()) && super.apply(channel);
	}

}
