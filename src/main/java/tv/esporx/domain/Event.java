package tv.esporx.domain;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import tv.esporx.collections.functions.Trimmer;
import tv.esporx.framework.string.MarkupKiller;
import tv.esporx.framework.validation.ValidHashtags;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

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
	@Length(max = 400000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	@NotNull
	@Column(name = "highlight", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean highlighted;
    @ValidHashtags
    @Column(name = "hashtags")
    private String twitterHashtags = "";

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

    public String getTwitterHashtags() {
        return twitterHashtags;
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

    public void setTwitterHashtags(String twitterHashtags) {
        List<String> hashtags = Arrays.asList(twitterHashtags.split(","));
        List<String> stringList = Lists.transform(hashtags, new Trimmer());

        this.twitterHashtags = Joiner.on(',').join(stringList);
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
