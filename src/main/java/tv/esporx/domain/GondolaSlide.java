package tv.esporx.domain;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "gondola_slides")
public class GondolaSlide {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "date", nullable = false)
	private DateTime date = new DateTime();
	@NotBlank
	@Length(max = 1000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@Column(name = "language", nullable = false)
	private String language = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "link", nullable = false)
	private String link = "";
	@NotBlank
	@Column(name = "picture", nullable = false)
	private String picture = "";
	@NotBlank
	@Column(name = "prize")
	@Length(max = 50)
	private String prize = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "tag_line", nullable = false)
	private String tagLine = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "title", nullable = false)
	private String title = "";

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GondolaSlide other = (GondolaSlide) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		}
		else if (!language.equals(other.language))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		}
		else if (!link.equals(other.link))
			return false;
		if (picture == null) {
			if (other.picture != null)
				return false;
		}
		else if (!picture.equals(other.picture))
			return false;
		if (prize == null) {
			if (other.prize != null)
				return false;
		}
		else if (!prize.equals(other.prize))
			return false;
		if (tagLine == null) {
			if (other.tagLine != null)
				return false;
		}
		else if (!tagLine.equals(other.tagLine))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}

	public Date getDate() {
		return date.toDate();
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public String getLanguage() {
		return language;
	}

	public String getLink() {
		return link;
	}

	public String getPicture() {
		return picture;
	}

	public String getPrize() {
		return prize;
	}

	public String getTagLine() {
		return tagLine;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((picture == null) ? 0 : picture.hashCode());
		result = prime * result + ((prize == null) ? 0 : prize.hashCode());
		result = prime * result + ((tagLine == null) ? 0 : tagLine.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	public void setDate(final Date date) {
		this.date = new DateTime(date);
	}

	public void setDescription(final String description) {
		checkArgument(description != null);
		this.description = description;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setPrize(final String prize) {
		this.prize = prize;
	}

	public void setTagLine(final String tagLine) {
		checkArgument(tagLine != null);
		this.tagLine = tagLine;
	}

	public void setTitle(final String title) {
		checkArgument(title != null);
		this.title = title;
	}

	@Override
	public String toString() {
		return "GondolaSlide [description=" + description + ", language=" + language + ", link=" + link + ", picture=" + picture + ", prize=" + prize + ", tagLine=" + tagLine + ", title=" + title + "]";
	}

}
