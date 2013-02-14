package tv.esporx.domain;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;
import tv.esporx.collections.functions.Trimmer;
import tv.esporx.framework.validation.SupportedLanguage;
import tv.esporx.framework.validation.ValidHashtags;
import tv.esporx.framework.validation.ValidTwitterId;

import javax.persistence.*;

import java.util.List;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "channels")
public class Channel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	@Column(name = "viewer_count_timestamp", nullable = true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime viewerCountTimestamp;
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	@NotBlank
	@Length(max = 400000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@NotBlank
	@URL(protocol = "http", message = "{channel.submission.error.url}")
	@Length(max = 255)
	@Column(name = "video_url", nullable = false, unique = true)
	private String videoUrl = "";
	@Column(name = "viewer_count", nullable = false)
	private int viewerCount;
	@SupportedLanguage
	@Column(name = "language", nullable = false)
	private String language = "";
    @ManyToOne(optional = false)
    @JoinColumn(name = "provider", nullable = false)
    private VideoProvider videoProvider;
    @ValidHashtags
    @Column(name = "twitter_hashtags")
    private String twitterHashtags = "";
    @ValidTwitterId
    @Column(name = "twitter_id")
    private String twitterId = "";
    
	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public int getViewerCount() {
		return viewerCount;
	}

	public void setTitle(final String title) {
		checkArgument(title != null);
		this.title = title;
	}

	public void setVideoUrl(final String videoUrl) {
		this.videoUrl = videoUrl;

	}

	public void setViewerCount(final int viewerCount) {
		this.viewerCount = viewerCount;
	}

	public void setLanguage(final String language) {
		checkArgument(language != null);
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setId(final long id) {
		checkArgument(id > 0L);
		this.id = id;
	}

	public DateTime getViewerCountTimestamp() {
		return viewerCountTimestamp;
	}

	public void setViewerCountTimestamp(DateTime viewerCountTimestamp) {
		this.viewerCountTimestamp = viewerCountTimestamp;
	}


    public VideoProvider getVideoProvider() {
        return this.videoProvider;
    }

    public String getTwitterHashtags() {
        return twitterHashtags;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setVideoProvider(VideoProvider videoProvider) {
        this.videoProvider = videoProvider;
    }

    public void setTwitterHashtags(String twitterHashtags) {
        String hashtagSequence = firstNonNull(twitterHashtags, "");
        List<String> stringList = transform(asList(hashtagSequence.split(",")), new Trimmer());
        this.twitterHashtags = Joiner.on(',').join(stringList);
    }

    public void setTwitterId(String twitterId) {
        String twitterIdSequence = firstNonNull(twitterId, "");
        this.twitterId = twitterIdSequence.trim();
    }

    public String getTwitterSearch() {
        String twitterId = firstNonNull(this.twitterId, "");
        String twitterHashtags = firstNonNull(this.twitterHashtags, "");
        boolean bothNotEmpty = (twitterHashtags != "" && twitterId != "");
        return new StringBuilder()
            .append(firstNonNull(twitterHashtags, ""))
            .append(bothNotEmpty ? "," : "")
            .append(firstNonNull(twitterId, ""))
            .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(language, title, videoUrl);
    }

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (obj == null || (getClass() != obj.getClass()))
			return false;

		Channel other = (Channel) obj;
		return equal(this.language, other.language) //
				&& equal(this.title, other.title) //
				&& equal(this.videoUrl, other.videoUrl);
	}

	@Override
	public String toString() {
		return toStringHelper(this) //
				.add("title", title) //
				.add("videoUrl", videoUrl) //
				.add("language", language) //
				.toString();
	}
}
