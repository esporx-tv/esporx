package tv.esporx.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Adding table for crawled event
 */
@Entity
@Table(name = "crawler_events")
public class CrawledEvent {
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
    @Column(name = "month", nullable = false)
    private String month;

    @NotBlank
    @Length(max = 250)
    @Column(name = "name", nullable = false, unique=true)
    private String name;

    @NotBlank
    @Length(max = 50)
    @Column(name = "prize_pool", nullable = false, unique=true)
    private String prizePool;

    @NotBlank
    @Column(name = "is_imported", nullable = false, unique=true)
    private boolean isImported;

    public CrawledEvent() {
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(String prizePool) {
        this.prizePool = prizePool;
    }

    public boolean getImported() {
        return isImported;
    }

    public void setImported(boolean imported) {
        isImported = imported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrawledEvent that = (CrawledEvent) o;

        if (id != that.id) return false;
        if (isImported != that.isImported) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (month != null ? !month.equals(that.month) : that.month != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (prizePool != null ? !prizePool.equals(that.prizePool) : that.prizePool != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (prizePool != null ? prizePool.hashCode() : 0);
        result = 31 * result + (isImported ? 1 : 0);
        return result;
    }
}