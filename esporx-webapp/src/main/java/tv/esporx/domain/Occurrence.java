package tv.esporx.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import tv.esporx.framework.validation.CrossDateConstraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableSet;
import static javax.persistence.GenerationType.IDENTITY;

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
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @ManyToMany
    @JoinTable(name = "occurrences_channels", //
    	joinColumns = {@JoinColumn(name="occurrence_id", referencedColumnName="id")},
    	inverseJoinColumns = {@JoinColumn(name="channel_id", referencedColumnName="id")})
    private Set<Channel> channels;

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

    public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Set<Channel> getChannels() {
		return unmodifiableSet(channels);
	}

	public void setChannels(Set<Channel> channels) {
		this.channels = channels;
	}
	
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Occurrence other = (Occurrence) obj;
        return equal(startDate, other.startDate) //
                && equal(endDate, other.endDate) //
                && equal(frequencyType, other.frequencyType) //
                && equal(channels, other.channels);
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((frequencyType == null) ? 0 : frequencyType.hashCode());
		result = prime * result + ((channels == null) ? 0 : channels.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return toStringHelper(this) //
				.add("startDate", startDate) //
				.add("endDate", endDate) //
				.add("frequencyType", frequencyType) //
				.add("channels", channels) //
				.toString();
	}
}
