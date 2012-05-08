package tv.esporx.services;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.joda.time.DateTime;

import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

/**
 * Determines if a cast can be "magnetized", i.e. if contained in the slot time
 * window and related to one of the current row events
 */
public class CastInSlotPredicate extends CastInRowPredicate {

	private final List<Event> eligibleEvents;

	public CastInSlotPredicate(final DateTime start, final DateTime end, final Iterable<Event> eligibleEvents) {
		super(start, end);
		this.eligibleEvents = newArrayList(eligibleEvents);
	}

	@Override
	public boolean apply(final Cast cast) {
		return eligibleEvents.contains(cast.getEvent()) && super.apply(cast);
	}

}
