package tv.esporx.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.unmodifiableList;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;

import tv.esporx.framework.validation.SupportedLanguage;

@Entity
@Table(name = "casts")
@NamedQueries({ @NamedQuery(name = "Cast.findAll", query = "FROM Cast cast ORDER BY cast.title ASC"), @NamedQuery(name = "Cast.findByTitle", query = "FROM Cast cast WHERE UPPER(cast.title) = :title"), @NamedQuery(name = "Cast.findMostViewed", query = "FROM Cast cast ORDER BY cast.viewerCount DESC"), @NamedQuery(name = "Cast.findTimeLine", query = "FROM Cast cast WHERE event IS NOT NULL AND cast.broadcastDate >= :date AND cast.broadcastDate <= :otherDate ORDER BY cast.broadcastDate ASC") })
public class Cast {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	@NotNull
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "broadcast_date", nullable = false)
	private DateTime broadcastDate = new DateTime();
	@ManyToMany
	@JoinTable(name = "cast_casters", joinColumns = { @JoinColumn(name = "castId") }, inverseJoinColumns = { @JoinColumn(name = "casterId") })
	private final List<User> casters = new ArrayList<User>();
	@NotNull
	@ManyToOne
	@JoinColumn(name = "related_game", nullable = false)
	private Game relatedGame = new Game();
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	@NotBlank
	@Length(max = 1000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@NotBlank
	@URL(protocol = "http", message = "{cast.submission.error.url}")
	@Length(max = 255)
	@Column(name = "video_url", nullable = false, unique = true)
	private String videoUrl = "";
	@Column(name = "viewer_count", nullable = false)
	private int viewerCount;
	@SupportedLanguage
	@Column(name = "language", nullable = false)
	private String language = "";
	@ManyToOne
	@JoinColumn(name = "related_event", nullable = true)
	private Event event;

	public void addCaster(final User user) {
		casters.add(user);
	}

	public Date getBroadcastDate() {
		return broadcastDate.toDate();
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

	public Game getRelatedGame() {
		return relatedGame;
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

	public void setBroadcastDate(final Date broadcastDate) {
		this.broadcastDate = new DateTime(broadcastDate);
	}

	public void setRelatedGame(final Game game) {
		checkArgument(game != null);
		this.relatedGame = game;
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

	public Event getEvent() {
		return event;
	}

	public void setEvent(final Event event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((videoUrl == null) ? 0 : videoUrl.hashCode());
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
		Cast other = (Cast) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		}
		else if (!language.equals(other.language))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		if (videoUrl == null) {
			if (other.videoUrl != null)
				return false;
		}
		else if (!videoUrl.equals(other.videoUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cast [title=" + title + ", videoUrl=" + videoUrl + ", language=" + language + "]";
	}

}
