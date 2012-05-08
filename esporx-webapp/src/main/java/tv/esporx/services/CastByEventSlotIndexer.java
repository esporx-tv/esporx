package tv.esporx.services;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Cast;
import tv.esporx.domain.front.EventSlot;

import com.google.common.base.Function;

@Service
final class CastByEventSlotIndexer implements Function<Cast, EventSlot> {
	@Override
	public EventSlot apply(final Cast cast) {
		return EventSlot.from(cast.getEvent());
	}
}