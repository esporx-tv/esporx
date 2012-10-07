package tv.esporx.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import tv.esporx.framework.validation.CrossDateConstraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "occurrences")
@CrossDateConstraints
public class Occurrence {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @NotNull
    @Column(name = "start_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startDate;
    @Column(name="end_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endDate;
    @ManyToOne
    @JoinColumn(name = "frequency_type", nullable = false)
    private FrequencyType frequencyType;

    public Occurrence(DateTime startDate, FrequencyType frequency) {
        checkNotNull(startDate);
        checkNotNull(frequency);
        this.startDate = startDate;
        this.frequencyType = frequency;
    }

    public Occurrence(DateTime startDate, DateTime endDate, FrequencyType frequency) {
        this(startDate, frequency);
        this.endDate = endDate;
        if(endDate.isBefore(startDate)) {
            throw new IllegalStateException("End must be after start");
        }
    }

    public long getId() {
        return id;
    }

    public boolean happensAt(DateTime dateTime) {
        return frequencyType.matches(startDate, endDate, dateTime);
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public FrequencyType getFrequencyType() {
        return frequencyType;
    }
}
