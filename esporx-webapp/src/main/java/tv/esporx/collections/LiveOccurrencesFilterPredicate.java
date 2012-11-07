package tv.esporx.collections;

import java.util.Set;

import org.joda.time.DateTime;

import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

import com.google.common.base.Predicate;

public class LiveOccurrencesFilterPredicate implements Predicate<Occurrence> {

	private static final int OFFLINE_CHANNEL_THRESHOLD = 50;

	@Override
	public boolean apply(Occurrence occ) {
		Set<Channel> channels = occ.getChannels();
		for (Channel channel : channels) {
			if (channel.getViewerCount() > OFFLINE_CHANNEL_THRESHOLD && channel.getViewerCountTimestamp().isAfter(new DateTime().minusMinutes(5))) {
				return true;
			}
		}
		return false;
	}

}
