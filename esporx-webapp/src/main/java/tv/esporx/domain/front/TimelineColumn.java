package tv.esporx.domain.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

public class TimelineColumn {

	private final DateTime start;
	private final List<TimelineRow> rows = new ArrayList<TimelineRow>();

	public TimelineColumn(final DateTime start) {
		this.start = start;
	}

	public void add(final TimelineRow rows) {
		this.rows.add(rows);
	}

	public Date getStart() {
		return start.toDate();
	}

	public List<TimelineRow> getRows() {
		return rows;
	}

	@Override
	public String toString() {
		return "DayTimeline [day=" + start + ", rows=" + rows + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimelineColumn other = (TimelineColumn) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		}
		else if (!start.equals(other.start))
			return false;
		return true;
	}

}
