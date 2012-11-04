package tv.esporx.collections;

import com.google.common.base.Function;
import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

public class ByHourOccurrenceIndexer implements Function<Occurrence, DateTime> {

    @Override
    public DateTime apply(Occurrence occurrence) {
        DateTime startDate = new DateTime(occurrence.getStartDate());
        return startDate.withTime(startDate.getHourOfDay(), 0, 0, 0);
    }
}
