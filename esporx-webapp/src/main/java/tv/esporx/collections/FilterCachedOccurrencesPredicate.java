package tv.esporx.collections;

import com.google.common.base.Predicate;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

import static tv.esporx.framework.time.DateTimeUtils.signedDiffInHours;

public class FilterCachedOccurrencesPredicate implements Predicate<DateTime> {

    private final DateTime timelineStart;

    public FilterCachedOccurrencesPredicate(DateTime timelineStart) {
        this.timelineStart = timelineStart;
    }

    @Override
    public boolean apply(DateTime cacheKey) {
        return signedDiffInHours(timelineStart, cacheKey) >= 0;
    }
}
