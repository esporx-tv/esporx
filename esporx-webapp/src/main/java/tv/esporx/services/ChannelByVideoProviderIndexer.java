package tv.esporx.services;

import tv.esporx.domain.Channel;
import tv.esporx.domain.VideoProvider;

import com.google.common.base.Function;

public class ChannelByVideoProviderIndexer implements Function<Channel, VideoProvider> {

    @Override
    public VideoProvider apply(Channel channel) {
        return channel.getVideoProvider();
    }
}