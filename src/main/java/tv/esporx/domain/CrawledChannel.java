package tv.esporx.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 *
 */
@Entity
@Table(name = "crawler_events")
public class CrawledChannel {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @NotBlank
    @Length(max = 500)
    @Column(name = "link", nullable = false)
    private String link;

    @NotBlank
    @Length(max = 50)
    @Column(name = "game_type", nullable = false)
    private String gameType;

    @NotBlank
    @Length(max = 250)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "num_viewers", nullable = false)
    private Long numViewers;

    @NotBlank
    @Column(name = "is_imported", nullable = false, unique=true)
    private boolean isImported;

    public CrawledChannel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumViewers() {
        return numViewers;
    }

    public void setNumViewers(Long numViewers) {
        this.numViewers = numViewers;
    }

    public boolean isImported() {
        return isImported;
    }

    public void setImported(boolean imported) {
        isImported = imported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrawledChannel that = (CrawledChannel) o;

        if (id != that.id) return false;
        if (isImported != that.isImported) return false;
        if (gameType != null ? !gameType.equals(that.gameType) : that.gameType != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (numViewers != null ? !numViewers.equals(that.numViewers) : that.numViewers != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (gameType != null ? gameType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (numViewers != null ? numViewers.hashCode() : 0);
        result = 31 * result + (isImported ? 1 : 0);
        return result;
    }
}
