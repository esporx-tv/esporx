package tv.esporx.domain.front;

import static com.google.common.base.Preconditions.checkNotNull;
import tv.esporx.domain.Event;
import tv.esporx.framework.string.MarkupKiller;

public class EventSlot {

	private final long id;
	private final String title;
	private final String description;

	private EventSlot(final long id, final String title, final String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public static EventSlot from(final Event event) {
		checkNotNull(event);
		return new EventSlot(event.getId(), event.getTitle(), event.getDescription());
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getStrippedDescription() {
		return new MarkupKiller().stripTags(description);
	}

	@Override
	public String toString() {
		return "EventSlot [id=" + id + ", name=" + title + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		EventSlot other = (EventSlot) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
			return false;
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
