package tv.esporx.dao.impl;

import static com.google.common.collect.Sets.newLinkedHashSet;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.esporx.dao.PersistenceCapableOccurrence;
import tv.esporx.domain.Occurrence;

@Repository
@Transactional
public class OccurrenceRepository implements PersistenceCapableOccurrence {

    private static final String DELETE_OCCURRENCE_CHANNEL_GIVEN_OCCURRENCE = "DELETE FROM occurrences_channels WHERE occurrence_id = ?";
    private static final String INSERT_OCCURRENCE_CHANNEL = "INSERT INTO occurrences_channels(occurrence_id, channel_id) VALUES (?, ?)";

    @PersistenceContext
    private EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OccurrenceRepository(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public Occurrence findById(final Long id) {
        return entityManager.find(Occurrence.class, id);
    }

    @Override
    public Long saveOrUpdate(final Occurrence occurrence, final List<Long> channelIds) {
        entityManager.persist(occurrence);
        long occurrenceId = occurrence.getId();

        executeBatchChannelInsert(occurrenceId, channelIds);
        return occurrenceId;
    }
    
	@Override
	public Collection<Occurrence> findByEventId(final Long eventId) {
		TypedQuery<Occurrence> query = entityManager.createNamedQuery("Occurrence.findByEventId", Occurrence.class);
        query.setParameter("eventId", eventId);
		return query.getResultList();
		
	}

    @Override
    public void delete(final Occurrence occurrence) {
        entityManager.remove(occurrence);
    }

    private void executeBatchChannelInsert(final long occurrenceId, final List<Long> channelIds) {
        jdbcTemplate.update(DELETE_OCCURRENCE_CHANNEL_GIVEN_OCCURRENCE, occurrenceId);
        jdbcTemplate.batchUpdate(INSERT_OCCURRENCE_CHANNEL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, occurrenceId);
                ps.setLong(2, channelIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return channelIds.size();
            }
        });
    }
}
