package tv.esporx.services;

import static com.google.common.collect.ImmutableSet.copyOf;

import java.util.Set;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Cast;
import tv.esporx.domain.Event;

import com.google.common.base.Function;

@Service
class CastAggregatorFunction implements Function<Event, Set<Cast>> {
	@Override
	public Set<Cast> apply(final Event event) {
		return copyOf(event.getCasts());
	}
}