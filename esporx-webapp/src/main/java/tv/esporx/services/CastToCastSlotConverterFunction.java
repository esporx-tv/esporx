package tv.esporx.services;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Cast;
import tv.esporx.domain.front.CastSlot;

import com.google.common.base.Function;

@Service
class CastToCastSlotConverterFunction implements Function<Cast, CastSlot> {
	@Override
	public CastSlot apply(final Cast cast) {
		return CastSlot.from(cast);
	}
}