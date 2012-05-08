package tv.esporx.domain.front;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import tv.esporx.domain.Cast;

public class CastSlot {

	private final long id;
	private final String title;
	private final Date broadcastDate;

	private CastSlot(final long id, final String title, final Date broadcastDate) {
		this.id = id;
		this.title = title;
		this.broadcastDate = broadcastDate;
	}

	public static CastSlot from(final Cast cast) {
		checkNotNull(cast);
		return new CastSlot(cast.getId(), cast.getTitle(), cast.getBroadcastDate());
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Date getBroadcastDate() {
		return broadcastDate;
	}

	@Override
	public String toString() {
		return "CastSlot [id=" + id + ", title=" + title + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		CastSlot other = (CastSlot) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}

}
