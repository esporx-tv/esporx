package tv.esporx.services;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

import com.google.common.base.Function;

@Service
public class CastToEventConverterFunction implements Function<Cast, Event> {

	@Override
	public Event apply(final Cast cast) {
		return cast.getEvent();
	}

}
