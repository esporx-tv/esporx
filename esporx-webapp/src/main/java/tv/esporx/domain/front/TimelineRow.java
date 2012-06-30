package tv.esporx.domain.front;

import static com.google.common.collect.ArrayListMultimap.create;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.common.collect.Multimap;

public class TimelineRow {

	private final DateTime start;
	private final DateTime end;
	private final Multimap<EventSlot, BroadcastSlot> eventSlots = create();

	public TimelineRow(final DateTime start, final DateTime end) {
		this.start = start;
		this.end = end;
	}

	public void putAll(final Multimap<EventSlot, BroadcastSlot> slots) {
		this.eventSlots.putAll(slots);
	}

	public Date getStart() {
		return start.toDate();
	}

	public Date getEnd() {
		return end.toDate();
	}

	public Map<EventSlot, Collection<BroadcastSlot>> getSlots() {
		return eventSlots.asMap();
	}

	@Override
	public String toString() {
		return "HourTimeline [start=" + start + ", end=" + end + ", events=" + eventSlots + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
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
		TimelineRow other = (TimelineRow) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		}
		else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		}
		else if (!start.equals(other.start))
			return false;
		return true;
	}

}
