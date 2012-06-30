package tv.esporx.domain.front;

import static com.google.common.base.Preconditions.checkNotNull;
import tv.esporx.domain.Broadcast;

public class BroadcastSlot {

	private final long id;
	private final String title;

	private BroadcastSlot(final long id, final String title) {
		this.id = id;
		this.title = title;
	}

	public static BroadcastSlot from(final Broadcast channel) {
		checkNotNull(channel);
		return new BroadcastSlot(channel.getId(), channel.getTitle());
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "ChannelSlot [id=" + id + ", title=" + title + "]";
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
		BroadcastSlot other = (BroadcastSlot) obj;
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
