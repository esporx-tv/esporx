package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import org.joda.time.DateTime;

import static tv.esporx.framework.time.DateTimeUtils.signedDiffInHours;

public class IsAfterStartHourFilter implements Predicate<DateTime> {

    private final DateTime timelineStart;

    public IsAfterStartHourFilter(DateTime timelineStart) {
        this.timelineStart = timelineStart;
    }

    @Override
    public boolean apply(DateTime cacheKey) {
        return signedDiffInHours(timelineStart, cacheKey) >= 0;
    }
}
