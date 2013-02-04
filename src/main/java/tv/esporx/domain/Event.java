package tv.esporx.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import tv.esporx.framework.string.MarkupKiller;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "events")
public class Event {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private long id;
	@NotBlank
	@Length(max = 1000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	@NotNull
	@Column(name = "highlight", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean highlighted;

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getStrippedDescription() {
		return new MarkupKiller().stripTags(description);
	}

    public void setId(final Long id) {
        this.id = id;
    }

    public void setDescription(final String description) {
        checkArgument(description != null);
        this.description = description;
    }

	public void setTitle(final String title) {
		checkArgument(title != null);
		this.title = title;
	}

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(final boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        return equal(description, other.description) //
                && equal(title, other.title);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return toStringHelper(this) //
				.add("description", description) //
				.add("title", title) //
				.toString();
	}
	
}