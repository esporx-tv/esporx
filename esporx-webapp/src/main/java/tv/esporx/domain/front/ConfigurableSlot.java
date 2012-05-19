package tv.esporx.domain.front;

import static javax.persistence.GenerationType.IDENTITY;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "configurable_slots")
@NamedQueries({ @NamedQuery(name = "ConfigurableSlot.findAll", query = "FROM ConfigurableSlot"), @NamedQuery(name = "ConfigurableSlot.findByLanguage", query = "FROM ConfigurableSlot WHERE active = TRUE AND language = :language"), @NamedQuery(name = "ConfigurableSlot.setOthersInactive", query = "UPDATE ConfigurableSlot SET active = FALSE WHERE position = :position AND language = :language AND id != :id") })
public class ConfigurableSlot {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	@Length(max = 1000)
	@Column(name = "description")
	private String description = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "link", nullable = false)
	private String link = "";
	@NotBlank
	@Length(max = 255)
	@Column(name = "picture", nullable = false)
	private String picture = "";
	@NotBlank
	@Length(max = 100)
	@Column(name = "title", nullable = false)
	private String title = "";
	@NotNull
	@Column(name = "position", nullable = false)
	private int position;
	@NotNull
	@Column(name = "is_active", nullable = false)
	private boolean active;
	@Column(name = "language", nullable = false)
	private String language;
	@Column(name = "box_title", nullable = false)
	private String boxTitle;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigurableSlot other = (ConfigurableSlot) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
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
		if (title == null) {
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public String getPicture() {
		return picture;
	}

	public String getTitle() {
		return title;
	}

	public int getPosition() {
		return position;
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((picture == null) ? 0 : picture.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setLink(final String link) {
		try {
			new URL(link);
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
		this.link = link;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "ConfigurableSlot [description=" + description + ", link=" + link + ", picture=" + picture + ", title=" + title + "]";
	}

	public String getBoxTitle() {
		return boxTitle;
	}

	public void setBoxTitle(String boxTitle) {
		this.boxTitle = boxTitle;
	}

}
