package tv.esporx.domain.front;

import static com.google.common.collect.ImmutableList.copyOf;

import java.util.ArrayList;
import java.util.List;


public class Timeline {

	private final List<TimelineColumn> columns = new ArrayList<TimelineColumn>();

	public void add(final TimelineColumn column) {
		columns.add(column);
	}

	public List<TimelineColumn> getColumns() {
		return copyOf(columns);
	}
}
