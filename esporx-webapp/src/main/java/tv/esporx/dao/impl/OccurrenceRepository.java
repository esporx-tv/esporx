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

    @PersistenceContext
    private EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OccurrenceRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    @Transactional(readOnly = true)
    public Occurrence findById(Long id) {
        return entityManager.find(Occurrence.class, id);
    }

    @Override
    public Long saveOrUpdate(Occurrence occurrence, List<Long> channelIds) {
        entityManager.persist(occurrence);
        long occurrenceId = occurrence.getId();

        executeBatchChannelInsert(occurrenceId, channelIds);
        return occurrenceId;
    }
    
	@Override
	public Collection<Occurrence> findByEventId(Long eventId) {
		TypedQuery<Occurrence> query = entityManager.createNamedQuery("Occurrence.findByEventId", Occurrence.class);
		query.setParameter("eventId", eventId);
		//FIXME
		return newLinkedHashSet(query.getResultList());
		
	}

    private void executeBatchChannelInsert(final long occurrenceId, final List<Long> channelIds) {
        jdbcTemplate.update("DELETE FROM occurrences_channels WHERE occurrence_id = ?", occurrenceId);
        jdbcTemplate.batchUpdate("INSERT INTO occurrences_channels(occurrence_id, channel_id) VALUES (?, ?)", new BatchPreparedStatementSetter() {
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


	@Override
	public void delete(Occurrence occurrence) {
		entityManager.remove(occurrence);
	}
}
