package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

public class BroadcastWithChannelPredicate implements Predicate<Occurrence> {

    private final Channel channel;

    public BroadcastWithChannelPredicate(Channel channel) {
        this.channel = channel;
    }


    @Override
    public boolean apply(Occurrence input) {
        return input.getChannels().contains(channel);
    }
}
