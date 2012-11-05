package tv.esporx.framework.time;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DateTimeUtilsTest {

    @Test
    public void should_compute_proper_start_date() {
        DateTime testDate = new DateTime().withTime(22,22,22,22);
        assertThat(DateTimeUtils.toStartDay(testDate)).isEqualTo(testDate.toDateMidnight());
    }

    @Test
    public void should_compute_proper_end_date() {
        DateTime testDate = new DateTime().withTime(22,22,22,22);
        assertThat(DateTimeUtils.toEndDay(testDate))
                .isEqualTo(testDate.withTime(23, 59, 59, 999));
    }

    @Test
    public void should_compute_start_hour() {
        DateTime testDate = new DateTime().withTime(22,22,22,22);
        assertThat(DateTimeUtils.toStartHour(testDate))
                .isEqualTo(testDate.withTime(22, 0, 0, 0));
    }

    @Test
    public void should_compute_difference_in_days() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = new DateTime().minusDays(2).withTime(10, 22, 22, 22);
        assertThat(DateTimeUtils.diffInDays(firstDate, secondDate)).isEqualTo(2);
    }

    @Test
    public void should_compute_difference_in_weeks() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = new DateTime().plusWeeks(2).withTime(23, 22, 22, 22);
        assertThat(DateTimeUtils.diffInWeeks(firstDate, secondDate)).isEqualTo(2);
    }

    @Test
    public void should_compute_difference_in_months() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = new DateTime().minusMonths(2).withTime(10, 22, 22, 22);
        assertThat(DateTimeUtils.diffInMonths(firstDate, secondDate)).isEqualTo(2);
    }

    @Test
    public void should_compute_difference_in_years() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = new DateTime().plusYears(2).withTime(23, 22, 22, 22);
        assertThat(DateTimeUtils.diffInYears(firstDate, secondDate)).isEqualTo(2);
    }

    @Test
    public void should_find_earliest_end_date() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = new DateTime().withTime(22, 22, 22, 21);
        assertThat(DateTimeUtils.earliestEnd(firstDate, secondDate))
                .isEqualTo(new DateTime().withTime(22, 22, 22, 21));
    }

    @Test
    public void should_find_null_as_earliest_end_date() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = null;
        assertThat(DateTimeUtils.earliestEnd(firstDate, secondDate))
                .isEqualTo(new DateTime().withTime(22,22,22,22));

        firstDate = null;
        secondDate = new DateTime().withTime(21,21,21,21);
        assertThat(DateTimeUtils.earliestEnd(firstDate, secondDate))
                .isEqualTo(new DateTime().withTime(21,21,21,21));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_earliest_end_fail_if_both_dates_are_null() {
        DateTimeUtils.earliestEnd(null, null);
    }

    @Test
    public void should_find_latest_beginning_date() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = new DateTime().withTime(22,22,22,21);
        assertThat(DateTimeUtils.latestBeginning(firstDate, secondDate))
                .isEqualTo(new DateTime().withTime(22,22,22,22));
    }

    @Test
    public void should_find_null_as_latest_beginning_date() {
        DateTime firstDate = new DateTime().withTime(22,22,22,22);
        DateTime secondDate = null;
        assertThat(DateTimeUtils.latestBeginning(firstDate, secondDate))
                .isEqualTo(new DateTime().withTime(22,22,22,22));

        firstDate = null;
        secondDate = new DateTime().withTime(21,21,21,21);
        assertThat(DateTimeUtils.latestBeginning(firstDate, secondDate))
                .isEqualTo(new DateTime().withTime(21,21,21,21));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_latest_beginning_fail_if_both_dates_are_null() {
        DateTimeUtils.latestBeginning(null, null);
    }
}
