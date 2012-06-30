package tv.esporx.services;

import tv.esporx.domain.Broadcast;

import com.google.common.base.Predicate;

class BroadcastWithNotNullEventPredicate implements Predicate<Broadcast> {
	@Override
	public boolean apply(final Broadcast channel) {
		return channel.getEvent() != null;
	}
}