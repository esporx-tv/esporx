package tv.esporx.services;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import tv.esporx.domain.Broadcast;


@Service
class BroadcastDateComparator implements Comparator<Broadcast> {
	@Override
	public int compare(final Broadcast firstChannel, final Broadcast secondChannel) {
		return firstChannel.getBroadcastDate().compareTo(secondChannel.getBroadcastDate());
	}
}