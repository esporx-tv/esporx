package tv.esporx.services;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.Condition;

import tv.esporx.domain.front.TimelineRow;

class ExpectedRowsWithSlotsCondition extends Condition<List<?>> {
	private final int expectedFullRowCount;
	private final List<TimelineRow> matchingRows = new ArrayList<TimelineRow>();
	private final int expectedSlotCountPerRow;

	public ExpectedRowsWithSlotsCondition(final int expectedFullRowCount, final int expectedSlotCountPerRow) {
		this.expectedFullRowCount = expectedFullRowCount;
		this.expectedSlotCountPerRow = expectedSlotCountPerRow;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(final List<?> rawRows) {
		List<TimelineRow> rows = (List<TimelineRow>) rawRows;
		int slotWithEventCount = 0;
		for (TimelineRow row : rows) {
			if (row.getSlots().size() == expectedSlotCountPerRow) {
				matchingRows.add(row);
				slotWithEventCount++;
			}
		}
		return slotWithEventCount == expectedFullRowCount;
	}

	public List<TimelineRow> getMatchingRows() {
		return matchingRows;

	}
}