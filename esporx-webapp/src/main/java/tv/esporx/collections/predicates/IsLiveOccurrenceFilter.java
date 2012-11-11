package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import tv.esporx.domain.Channel;
import tv.esporx.domain.Occurrence;

import java.util.Set;

import static com.google.common.collect.Sets.filter;

public class IsLiveOccurrenceFilter implements Predicate<Occurrence> {

	@Override
	public boolean apply(Occurrence occ) {
        Set<Channel> filter = filter(occ.getChannels(), new IsLiveChannelFilter());
        occ.setChannels(filter);
        return filter.size() > 0;
	}

}
