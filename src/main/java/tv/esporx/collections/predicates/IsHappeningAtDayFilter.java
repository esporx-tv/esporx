package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import org.joda.time.DateTime;

import static tv.esporx.framework.time.DateTimeUtils.toStartDay;

public class IsHappeningAtDayFilter implements Predicate<DateTime> {

    private final DateTime day;

    public IsHappeningAtDayFilter(DateTime day) {
        this.day = toStartDay(day);
    }

    @Override
    public boolean apply(DateTime input) {
        return day.isEqual(toStartDay(input));
    }
}
