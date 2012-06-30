package tv.esporx.services;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Broadcast;
import tv.esporx.domain.front.EventSlot;

import com.google.common.base.Function;

@Service
final class BroadcastByEventSlotIndexer implements Function<Broadcast, EventSlot> {
	@Override
	public EventSlot apply(final Broadcast channel) {
		return EventSlot.from(channel.getEvent());
	}
}