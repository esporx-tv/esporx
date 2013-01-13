package tv.esporx.collections.functions;

import com.google.common.base.Function;
import org.joda.time.DateTime;
import tv.esporx.domain.Occurrence;

public class ByDayOccurrenceIndexer  implements Function<Occurrence, DateTime> {

    @Override
    public DateTime apply(Occurrence occurrence) {
        DateTime startDate = new DateTime(occurrence.getStartDate());
        return startDate.withTimeAtStartOfDay();
    }
}

