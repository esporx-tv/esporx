package tv.esporx.domain.front;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;
import tv.esporx.framework.time.Duration;

/**
 * A timeline is made of rows (time interval, e.g. every 30 minutes) and columns
 * (e.g. 1 column = 1 day). Each slot can span over several rows but is
 * comprised within 1 column only.
 */
public class TimelineDimensions {
	private static final Duration DEFAULT_MAX_SLOT_HEIGHT = Duration.of(120, MINUTES);
	private static final Duration DEFAULT_MAX_COLUMN_HEIGHT = Duration.of(1, DAYS);
	private static final Duration DEFAULT_MAX_TOTAL_WIDTH = Duration.of(3, DAYS);
	private static final Duration DEFAULT_ROW_INTERVAL = Duration.of(30, MINUTES);
	private static final Duration DEFAULT_COLUMN_INTERVAL = Duration.of(1, DAYS);

	private Duration maxSlotHeight;
	private Duration maxColumnHeight;
	private Duration maxWidth;
	private Duration rowInterval;
	private Duration columnInterval;

	public TimelineDimensions() {
		this.maxSlotHeight = DEFAULT_MAX_SLOT_HEIGHT;
		this.maxColumnHeight = DEFAULT_MAX_COLUMN_HEIGHT;
		this.maxWidth = DEFAULT_MAX_TOTAL_WIDTH;
		this.rowInterval = DEFAULT_ROW_INTERVAL;
		this.columnInterval = DEFAULT_COLUMN_INTERVAL;
	}

	public Duration getMaxSlotHeight() {
		return maxSlotHeight;
	}

	public Duration getMaxColumnHeight() {
		return maxColumnHeight;
	}

	public Duration getMaxWidth() {
		return maxWidth;
	}

	public Duration getRowInterval() {
		return rowInterval;
	}

	public Duration getColumnInterval() {
		return columnInterval;
	}

	public TimelineDimensions withMaxSlotHeight(final Duration maxSlotHeight) {
		checkArgument(maxSlotHeight.compareTo(rowInterval) >= 0);
		checkArgument(maxColumnHeight.compareTo(maxSlotHeight) >= 0);
		this.maxSlotHeight = maxSlotHeight;
		return this;
	}

	public TimelineDimensions withMaxColumnHeight(final Duration maxColumnHeight) {
		checkArgument(maxColumnHeight.compareTo(maxSlotHeight) >= 0);
		this.maxColumnHeight = maxColumnHeight;
		return this;
	}

	public TimelineDimensions withMaxWidth(final Duration maxWidth) {
		checkArgument(maxWidth.compareTo(columnInterval) >= 0);
		this.maxWidth = maxWidth;
		return this;
	}

	public TimelineDimensions withRowInterval(final Duration rowInterval) {
		checkArgument(maxSlotHeight.compareTo(rowInterval) >= 0);
		this.rowInterval = rowInterval;
		return this;
	}

	public TimelineDimensions withColumnInterval(final Duration columnInterval) {
		checkArgument(maxWidth.compareTo(columnInterval) >= 0);
		this.columnInterval = columnInterval;
		return this;
	}
}