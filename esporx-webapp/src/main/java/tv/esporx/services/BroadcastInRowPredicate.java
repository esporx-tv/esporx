package tv.esporx.services;

import org.joda.time.DateTime;

import tv.esporx.domain.Broadcast;

import com.google.common.base.Predicate;

/**
 * Determines if a given channel is within the given row time window
 */
class BroadcastInRowPredicate implements Predicate<Broadcast> {
	protected final DateTime start;
	protected final DateTime end;

	public BroadcastInRowPredicate(final DateTime start, final DateTime end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean apply(final Broadcast channel) {
		DateTime broadcastDate = new DateTime(channel.getBroadcastDate());
		return start.isBefore(broadcastDate.plusMillis(1)) && end.isAfter(broadcastDate.minusMillis(1));
	}
}