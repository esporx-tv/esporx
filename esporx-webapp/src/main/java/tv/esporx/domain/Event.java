package tv.esporx.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import tv.esporx.framework.validation.CrossDateConstraints;

@Entity
@Table(name = "events")
@NamedQueries({ @NamedQuery(name = "Event.findUpNext", query = "FROM Event WHERE startDate > :date"), @NamedQuery(name = "Event.findAll", query = "FROM Event"), @NamedQuery(name = "Event.findTimeLine", query = "FROM Event WHERE startDate > :date AND startDate < :otherDate ") })
@CrossDateConstraints
public class Event {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private long id;
	@NotBlank
	@Length(max = 1000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@NotNull
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "start_date", nullable = false)
	private DateTime startDate = new DateTime();
	@NotNull
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "end_date", nullable = false)
	private DateTime endDate = new DateTime();
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	// TODO: no EAGER, specific JPQL with fetch instead!
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "related_event")
	private final List<Cast> casts = new ArrayList<Cast>();

	public void addCast(final Cast cast) {
		casts.add(cast);
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
		if (description == null) {
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}

	public Cast getCast(final int index) {
		return casts.get(index);
	}

	public List<Cast> getCasts() {
		return casts;
	}

	public String getDescription() {
		return description;
	}

	public Date getEndDate() {
		return endDate.toDate();
	}

	public long getId() {
		return id;
	}

	public Date getStartDate() {
		return startDate.toDate();
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	public void setDescription(final String description) {
		checkArgument(description != null);
		this.description = description;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = new DateTime(endDate);
	}

	public void setStartDate(final Date startDate) {
		this.startDate = new DateTime(startDate);
	}

	public void setTitle(final String title) {
		checkArgument(title != null);
		this.title = title;
	}

	@Override
	public String toString() {
		return "Event [description=" + description + ", title=" + title + "]";
	}

}
