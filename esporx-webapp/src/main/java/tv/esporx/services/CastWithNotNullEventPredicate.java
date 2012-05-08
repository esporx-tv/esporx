package tv.esporx.services;

import tv.esporx.domain.Cast;

import com.google.common.base.Predicate;

class CastWithNotNullEventPredicate implements Predicate<Cast> {
	@Override
	public boolean apply(final Cast cast) {
		return cast.getEvent() != null;
	}
}