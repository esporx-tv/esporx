package tv.esporx.collections.predicates;

import org.junit.Test;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

import static com.google.common.collect.Sets.newHashSet;
import static org.fest.assertions.Assertions.assertThat;

public class BroadcastWithChannelPredicateTest {

    @Test
    public void should_match_channel() {
        Occurrence occurrence = new Occurrence();
        Channel channel = new Channel();
        channel.setTitle("caca");
        Channel channel2 = new Channel();
        channel2.setTitle("caca");
        occurrence.setChannels(newHashSet(channel));
        assertThat(new BroadcastWithChannelPredicate(channel2).apply(occurrence)).isTrue();
    }

    @Test
    public void should_not_match_channel() {
        Occurrence occurrence = new Occurrence();
        Channel channel = new Channel();
        channel.setTitle("caca");
        Channel channel2 = new Channel();
        channel2.setTitle("pipi");
        occurrence.setChannels(newHashSet(channel));
        assertThat(new BroadcastWithChannelPredicate(channel2).apply(occurrence)).isFalse();
    }
}
