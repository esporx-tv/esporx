package tv.esporx.services;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Cast;


@Service
class CastBroadcastDateComparator implements Comparator<Cast> {
	@Override
	public int compare(final Cast firstCast, final Cast secondCast) {
		return firstCast.getBroadcastDate().compareTo(secondCast.getBroadcastDate());
	}
}