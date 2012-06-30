package tv.esporx.services;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Broadcast;
import tv.esporx.domain.front.BroadcastSlot;

import com.google.common.base.Function;

@Service
class BroadcastToBroadcastSlotConverterFunction implements Function<Broadcast, BroadcastSlot> {
	@Override
	public BroadcastSlot apply(final Broadcast channel) {
		return BroadcastSlot.from(channel);
	}
}