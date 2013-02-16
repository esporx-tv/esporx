package tv.esporx.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "games")
public class Game {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private long id;
	@NotBlank
	@Length(max = 1000)
	@Column(name = "description", nullable = false)
	private String description = "";
	@NotBlank
	@Length(max = 100)
	@Column(name = "title", nullable = false, unique = true)
	private String title = "";
	@Length(max = 255)
	@Column(name = "icon_url", nullable = false)
	private String iconUrl = "";

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(final String description) {
		checkArgument(description != null);
		this.description = description;
	}

	public void setTitle(final String title) {
		checkArgument(title != null);
		this.title = title;
	}

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
