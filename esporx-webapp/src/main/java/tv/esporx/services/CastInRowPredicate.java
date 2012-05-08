package tv.esporx.services;

import org.joda.time.DateTime;

import tv.esporx.domain.Cast;

import com.google.common.base.Predicate;

/**
 * Determines if a given cast is within the given row time window
 */
class CastInRowPredicate implements Predicate<Cast> {
	protected final DateTime start;
	protected final DateTime end;

	public CastInRowPredicate(final DateTime start, final DateTime end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean apply(final Cast cast) {
		DateTime broadcastDate = new DateTime(cast.getBroadcastDate());
		return start.isBefore(broadcastDate.plusMillis(1)) && end.isAfter(broadcastDate.minusMillis(1));
	}
}