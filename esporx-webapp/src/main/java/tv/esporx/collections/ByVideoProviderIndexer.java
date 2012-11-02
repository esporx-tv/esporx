package tv.esporx.collections;

import com.google.common.base.Function;
import tv.esporx.domain.Channel;
import tv.esporx.domain.VideoProvider;

public class ByVideoProviderIndexer implements Function<Channel, VideoProvider> {

    @Override
    public VideoProvider apply(Channel channel) {
        return channel.getVideoProvider();
    }
}