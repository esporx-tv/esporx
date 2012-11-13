package tv.esporx.collections.comparators;

import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

import java.util.Comparator;
import java.util.Set;

import static com.google.common.collect.Sets.newTreeSet;

public class OccurrenceByMaxViewerComparator implements Comparator<Occurrence> {
    @Override
    public int compare(Occurrence occ1, Occurrence occ2) {
        Integer firstMaxViewerCount = getMaxViewerCount(occ1);
        Integer secondMaxViewerCount = getMaxViewerCount(occ2);
        return firstMaxViewerCount.compareTo(secondMaxViewerCount);
    }

    private Integer getMaxViewerCount(Occurrence occurrence) {
    	if(occurrence.getChannels().isEmpty()) {
    		return 0;
    	}
        Set<Channel> sortedChannels = newTreeSet(new ChannelByMaxViewerComparator());
        sortedChannels.addAll(occurrence.getChannels());
        return Integer.valueOf(sortedChannels.iterator().next().getViewerCount());
    }
}
