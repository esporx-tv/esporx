package tv.esporx.domain;

import static org.fest.assertions.Assertions.assertThat;
import static tv.esporx.domain.FrequencyType.FrequencyTypeValues.DAILY;
import static tv.esporx.domain.FrequencyType.FrequencyTypeValues.MONTHLY;
import static tv.esporx.domain.FrequencyType.FrequencyTypeValues.ONCE;
import static tv.esporx.domain.FrequencyType.FrequencyTypeValues.WEEKLY;
import static tv.esporx.domain.FrequencyType.FrequencyTypeValues.YEARLY;

import org.joda.time.DateTime;
import org.junit.Test;

public class OccurrenceTest {

    @Test
    public void should_match_exact_start_date() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new FrequencyType(ONCE));
        assertThat(occurrence.happensAt(new DateTime(10000000))).isTrue();
    }

    @Test
    public void should_not_match_after_end_date() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), new FrequencyType(ONCE));
        assertThat(occurrence.happensAt(new DateTime(30000000))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), new FrequencyType(DAILY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusDays(20))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), new FrequencyType(WEEKLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusWeeks(20))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), new FrequencyType(MONTHLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusMonths(20))).isFalse();

        occurrence = new Occurrence(new DateTime(10000000), new DateTime(20000000), new FrequencyType(YEARLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusYears(20))).isFalse();
    }

    @Test
    public void should_match_start_date_regardless_seconds() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), new FrequencyType(ONCE));
        assertThat(occurrence.happensAt(new DateTime(10000000))).isTrue();
    }

    @Test
    public void should_match_one_daily_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new FrequencyType(DAILY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusDays(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), new FrequencyType(DAILY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusDays(3))).isTrue();
    }

    @Test
    public void should_match_one_weekly_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new FrequencyType(WEEKLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusWeeks(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), new FrequencyType(WEEKLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusWeeks(3))).isTrue();
    }

    @Test
    public void should_match_one_monthly_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new FrequencyType(MONTHLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusMonths(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), new FrequencyType(MONTHLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusMonths(3))).isTrue();
    }

    @Test
    public void should_match_one_yearly_occurrence() {
        Occurrence occurrence = new Occurrence(new DateTime(10000000), new FrequencyType(YEARLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusYears(3))).isTrue();

        occurrence = new Occurrence(new DateTime(10000000).plusSeconds(3), new FrequencyType(YEARLY));
        assertThat(occurrence.happensAt(new DateTime(10000000).plusYears(3))).isTrue();
    }
}
