package tv.esporx.framework.time;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

public class Duration implements Comparable<Duration> {

	private final int duration;
	private final TimeUnit unit;

	private Duration(final int duration, final TimeUnit unit) {
		this.duration = duration;
		this.unit = unit;
	}

	public static Duration of(final int duration, final TimeUnit unit) {
		return new Duration(duration, unit);
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public DateTime plus(final DateTime datetime) {
		return datetime.plusMillis(asMilliseconds());
	}

	public DateTime minusFrom(final DateTime datetime) {
		return datetime.minusMillis(asMilliseconds());
	}

	public int asMilliseconds() {
		/*
		 * this narrowing conversion is acceptable given the duration amount
		 * should not go over int capacity
		 */
		return (int) unit.toMillis(duration);
	}

	@Override
	public int compareTo(final Duration other) {
		return asMilliseconds() - other.asMilliseconds();
	}

	@Override
	public String toString() {
		return "Duration [duration=" + duration + ", unit=" + unit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + asMilliseconds();
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
		return this.compareTo((Duration) obj) == 0;
	}

}
