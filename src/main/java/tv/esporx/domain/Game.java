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
    @NotBlank
	@Length(max = 255)
	@Column(name = "icon_url", nullable = false)
	private String iconUrl = "";
	@Length(max = 255)
	@Column(name = "background_url", nullable = true)
	private String backgroundUrl = "";

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

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (description != null ? !description.equals(game.description) : game.description != null) return false;
        if (iconUrl != null ? !iconUrl.equals(game.iconUrl) : game.iconUrl != null) return false;
        if (title != null ? !title.equals(game.title) : game.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        return result;
    }
}
