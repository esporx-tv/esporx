package tv.esporx.collections.comparators;

import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import static com.google.common.collect.Sets.newTreeSet;

public class OccurrenceByMaxViewerComparator implements Comparator<Occurrence> {
    @Override
    public int compare(Occurrence occ1, Occurrence occ2) {
        Integer firstMaxViewerCount = getMaxViewerCount(occ1);
        Integer secondMaxViewerCount = getMaxViewerCount(occ2);
        return firstMaxViewerCount.compareTo(secondMaxViewerCount);
    }

    private Integer getMaxViewerCount(Occurrence occ1) {
        Set<Channel> channels = occ1.getChannels();
        if (channels.isEmpty()) {
            return 0;
        }
        else {
            SortedSet<Channel> sortedChannels = newTreeSet(new ChannelByMaxViewerComparator());
            sortedChannels.addAll(channels);
            return sortedChannels.first().getViewerCount();
        }
    }
}
