package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import org.joda.time.DateTime;
import tv.esporx.domain.Channel;

public class IsLiveChannelFilter implements Predicate<Channel> {

    private static final int OFFLINE_CHANNEL_THRESHOLD = 50;

    @Override
    public boolean apply(Channel channel) {
        return channel.getViewerCount() > OFFLINE_CHANNEL_THRESHOLD && channel.getViewerCountTimestamp().isAfter(new DateTime().minusMinutes(5));
    }
}
