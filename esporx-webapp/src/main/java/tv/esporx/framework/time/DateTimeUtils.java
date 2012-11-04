package tv.esporx.framework.time;

import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static org.joda.time.Days.daysBetween;
import static org.joda.time.Months.monthsBetween;
import static org.joda.time.Weeks.weeksBetween;
import static org.joda.time.Years.yearsBetween;

public class DateTimeUtils {

    /**
     * Resets time of provided dateTime to midnight
     */
    public static DateTime toStartDay(DateTime dateTime) {
        return dateTime.withTimeAtStartOfDay();
    }

    /**
     * Resets time of provided dateTime to (D+1)midnight-1ms
     */
    public static DateTime toEndDay(DateTime dateTime) {
        return dateTime.withTime(23, 59, 59, 999);
    }

    /**
     * Resets time of provided dateTime to (D+1)midnight-1ms
     */
    public static DateTime toStartHour(DateTime dateTime) {
        return dateTime.withTime(dateTime.getHourOfDay(), 0, 0, 0);
    }


    public static int diffInDays(DateTime start, DateTime end) {
        return daysBetween(start, end).getDays();
    }

    public static int diffInWeeks(DateTime start, DateTime end) {
        return weeksBetween(start, end).getWeeks();
    }

    public static int diffInMonths(DateTime start, DateTime end) {
        return monthsBetween(start, end).getMonths();
    }

    public static int diffInYears(DateTime start, DateTime end) {
        return yearsBetween(start, end).getYears();
    }

    public static DateTime earliest(DateTime one, DateTime two) {
        checkArgument((one != null) ^ (two != null));

        DateTime first;
        if(one == null) {
            first = two;
        }
        else if(two == null) {
            first = one;
        }
        else {
            first = (one.isBefore(two)) ? one : two;
        }
        return first;
    }
}
