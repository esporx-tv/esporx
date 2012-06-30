package tv.esporx.domain;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.unmodifiableList;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import tv.esporx.framework.validation.SupportedLanguage;

import com.google.inject.internal.Objects;

@Entity
@Table(name = "channels")
@NamedQueries({ //
	/**/@NamedQuery(name = "Channel.findAll", query = "FROM Channel channel ORDER BY channel.title ASC"), //
	@NamedQuery(name = "Channel.findByTitle", query = "FROM Channel channel WHERE UPPER(channel.title) = :title"), //
	@NamedQuery(name = "Channel.findMostViewed", query = "FROM Channel channel ORDER BY channel.viewerCount DESC"), //
	@NamedQuery(name = "Channel.findTimeLine", query = "FROM Channel channel WHERE event IS NOT NULL") //
})
public class Channel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	@Column(name = "is_live", nullable = false)
	private boolean live;
	@ManyToMany
	@JoinTable(name = "channel_casters", joinColumns = { @JoinColumn(name = "channelId") }, inverseJoinColumns = { @JoinColumn(name = "casterId") })
	private final List<User> casters = new ArrayList<User>();
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	@NotBlank
	@Length(max = 1000)
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

	public void addCaster(final User user) {
		casters.add(user);
	}

	public User getCaster(final int index) {
		return casters.get(index);
	}

	public List<User> getCasters() {
		return unmodifiableList(casters);
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<User> getUsers() {
		return casters;
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

	public boolean isLive() {
		return live;
	}

	public void setLive(final boolean live) {
		this.live = live;
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
