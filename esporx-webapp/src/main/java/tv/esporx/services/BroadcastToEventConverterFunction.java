package tv.esporx.services;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Broadcast;
import tv.esporx.domain.Event;

import com.google.common.base.Function;

@Service
public class BroadcastToEventConverterFunction implements Function<Broadcast, Event> {

	@Override
	public Event apply(final Broadcast channel) {
		return channel.getEvent();
	}

}
