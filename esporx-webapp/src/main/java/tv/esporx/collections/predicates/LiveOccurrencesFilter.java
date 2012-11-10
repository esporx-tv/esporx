package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import org.joda.time.DateTime;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

import java.util.Set;

public class LiveOccurrencesFilter implements Predicate<Occurrence> {

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
