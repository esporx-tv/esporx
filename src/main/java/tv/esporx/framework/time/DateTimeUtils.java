package tv.esporx.framework.time;

import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.joda.time.Days.daysBetween;
import static org.joda.time.Hours.hoursBetween;
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


    /**
     * Computes the *signed* value of immediate inferior integer of number of days between two dates
     */
    public static int signedDiffInHours(DateTime start, DateTime end) {
        return hoursBetween(start, end).getHours();
    }


    /**
     * Computes the absolute value of immediate inferior integer of number of days between two dates
     */
    public static int diffInDays(DateTime start, DateTime end) {
        return Math.abs(daysBetween(start, end).getDays());
    }


    /**
     * Computes the absolute value of immediate inferior integer of number of weeks between two dates
     */
    public static int diffInWeeks(DateTime start, DateTime end) {
        return Math.abs(weeksBetween(start, end).getWeeks());
    }


    /**
     * Computes the absolute value of immediate inferior integer of number of months between two dates
     */
    public static int diffInMonths(DateTime start, DateTime end) {
        return Math.abs(monthsBetween(start, end).getMonths());
    }


    /**
     * Computes the absolute value of immediate inferior integer of number of years between two dates
     */
    public static int diffInYears(DateTime start, DateTime end) {
        return Math.abs(yearsBetween(start, end).getYears());
    }


    /**
     * Computes the earliest dates of both provided
     * @throws NullPointerException if ANY of them is null
     */
    public static DateTime earliest(DateTime start, DateTime end) {
        checkNotNull(start);
        checkNotNull(end);

        return start.isBefore(end) ? start : end;
    }

    /**
     * Computes the earliest of the two end dates.
     * Either of them can be null, but not both.
     * In this case, the non-null date is considered the earliest end.
     * @throws IllegalArgumentException if both dates are null
     */
    public static DateTime earliestEnd(DateTime one, DateTime two) {
        checkMaxOneDateNull(one, two);

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

    /**
     * Computes the latest of the two beginning dates.
     * Either of them can be null, but not both.
     * In this case, the non-null date is considered the latest beginning.
     * @throws IllegalArgumentException if both dates are null
     */
    public static DateTime latestBeginning(DateTime one, DateTime two) {
        checkMaxOneDateNull(one, two);

        DateTime last;
        if(one == null) {
            last = two;
        }
        else if(two == null) {
            last = one;
        }
        else {
            last = (one.isAfter(two)) ? one : two;
        }
        return last;
    }

    private static void checkMaxOneDateNull(DateTime one, DateTime two) {
        checkArgument(!((one == null) && (two == null)), "Only one date can be null!");
    }
}
