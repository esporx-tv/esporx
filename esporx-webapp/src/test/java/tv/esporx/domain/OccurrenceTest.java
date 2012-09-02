package tv.esporx.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.domain.FrequencyType.*;

public class OccurrenceTest {

    @Test
    public void should_match_exact_start_date() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), ONCE);
        assertThat(occurrence.happensAt(new DateTime(10000000))).isTrue();
    }

    @Test
    public void should_not_match_after_end_date() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), ONCE);
        assertThat(occurrence.happensAt(new DateTime(30000000))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), DAILY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusDays(20))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), WEEKLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusWeeks(20))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), MONTHLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusMonths(20))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), YEARLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusYears(20))).isFalse();
    }

    @Test
    public void should_match_start_date_regardless_seconds() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), ONCE);
        assertThat(occurrence.happensAt(new DateTime(10000000))).isTrue();
    }

    @Test
    public void should_match_one_daily_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), DAILY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusDays(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), DAILY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusDays(3))).isTrue();
    }

    @Test
    public void should_match_one_weekly_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), WEEKLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusWeeks(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), WEEKLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusWeeks(3))).isTrue();
    }

    @Test
    public void should_match_one_monthly_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), MONTHLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusMonths(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), MONTHLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusMonths(3))).isTrue();
    }

    @Test
    public void should_match_one_yearly_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), YEARLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusYears(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), YEARLY);
        assertThat(occurrence.happensAt(new DateTime(10000000).plusYears(3))).isTrue();
    }
}
