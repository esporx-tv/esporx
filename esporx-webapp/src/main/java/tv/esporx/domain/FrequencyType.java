package tv.esporx.domain;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public enum FrequencyType {
    ONCE, DAILY, WEEKLY, MONTHLY, YEARLY;

    public boolean matches(DateTime start, DateTime end, DateTime actual) {
        start = start.withSecondOfMinute(0).withMillisOfSecond(0);
        actual = actual.withSecondOfMinute(0).withMillisOfSecond(0);

        if(end != null) {
            end = end.withSecondOfMinute(0).withMillisOfSecond(0);
            if(actual.isAfter(end)) {
                return false;
            }
        }

        switch(this) {
            case ONCE:
                return start.isEqual(actual);
            case DAILY:
                return matchesDaily(start, actual);
            case WEEKLY:
                return matchesWeekly(start, actual);
            case MONTHLY:
                return matchesMontly(start, actual);
            case YEARLY:
                return matchesYearly(start, actual);
        }
        throw new IllegalStateException("WTF? Occurrence type not supported.");
    }

    private boolean matchesYearly(DateTime start, DateTime actual) {
        long hourDifference = getDifferenceInHours(start, actual);
        return hourDifference > 0 && ((((hourDifference % 24) % 7) % 4) % 52)== 0;
    }

    private boolean matchesMontly(DateTime start, DateTime actual) {
        long hourDifference = getDifferenceInHours(start, actual);
        return hourDifference > 0 && (((hourDifference % 24) % 7) % 4) == 0;
    }

    private boolean matchesWeekly(DateTime start, DateTime actual) {
        long hourDifference = getDifferenceInHours(start, actual);
        return hourDifference > 0 && ((hourDifference % 24) % 7) == 0;
    }

    private boolean matchesDaily(DateTime start, DateTime actual) {
        long hourDifference = getDifferenceInHours(start, actual);
        return hourDifference > 0 && (hourDifference % 24) == 0;
    }

    private long getDifferenceInHours(DateTime start, DateTime actual) {
        return new Duration(start, actual).getStandardHours();
    }
}
