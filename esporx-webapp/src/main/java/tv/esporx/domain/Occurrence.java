package tv.esporx.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonMethod;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import tv.esporx.framework.validation.CrossDateConstraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.persistence.GenerationType.IDENTITY;
import static org.codehaus.jackson.annotate.JsonMethod.SETTER;

@Entity
@Table(name = "occurrences")
@CrossDateConstraints(nullableColumns = {"endDate"})
public class Occurrence {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
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

    /*JPA*/
    public Occurrence() {}

    public Occurrence(DateTime startDate, FrequencyType frequency) {
        checkNotNull(startDate);
        checkNotNull(frequency);
        this.startDate = startDate;
        this.frequencyType = frequency;
    }

    public Occurrence(DateTime startDate, DateTime endDate, FrequencyType frequencyType) {
        this(startDate, frequencyType);
        if(endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("End must be after start");
        }
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public boolean happensAt(DateTime dateTime) {
        return frequencyType.matches(startDate, endDate, dateTime);
    }

    public Date getStartDate() {
        return startDate.toDate();
    }

    public Date getEndDate() {
        return (endDate == null) ? null : endDate.toDate();
    }

    public FrequencyType getFrequencyType() {
        return frequencyType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = new DateTime(startDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate == null ? null : new DateTime(endDate);
    }

    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }
}
