package tv.esporx.domain;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.persistence.*;

@Entity
@Table(name = "frequency_types")
@NamedQueries(
        @NamedQuery(name = "FrequencyType.findAll", query = "FROM FrequencyType ORDER BY value ASC")
)
public class FrequencyType {

    public enum FrequencyTypeValues {
        ONCE, DAILY, WEEKLY, MONTHLY, YEARLY;
    }

    private String value;

    public FrequencyType() {}
    public FrequencyType(FrequencyTypeValues value) {
        this.value = value.name();
    }

    @Id
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        //validates value against the enumerated/allowed values (ie throws exceptions if invalid value)
        FrequencyTypeValues.valueOf(value.toUpperCase());
        this.value = value;
    }

    public boolean matches(DateTime start, DateTime end, DateTime testDate) {
        start = start.withSecondOfMinute(0).withMillisOfSecond(0);
        testDate = testDate.withSecondOfMinute(0).withMillisOfSecond(0);

        if(end != null) {
            end = end.withSecondOfMinute(0).withMillisOfSecond(0);
            if(testDate.isAfter(end)) {
                return false;
            }
        }

        return matches(start, testDate);
    }

    private boolean matches(DateTime start, DateTime actual) {
        switch(FrequencyTypeValues.valueOf(value.toUpperCase())) {
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
