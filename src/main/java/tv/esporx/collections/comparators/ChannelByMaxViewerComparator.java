package tv.esporx.collections.comparators;

import tv.esporx.domain.Channel;

import java.util.Comparator;

import static java.lang.Integer.valueOf;

public class ChannelByMaxViewerComparator implements Comparator<Channel> {
    @Override
    public int compare(Channel channel1, Channel channel2) {
        return valueOf(channel2.getViewerCount()).compareTo(valueOf(channel1.getViewerCount()));
    }
}
