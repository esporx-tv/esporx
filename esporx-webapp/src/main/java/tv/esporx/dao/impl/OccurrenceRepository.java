package tv.esporx.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tv.esporx.dao.PersistenceCapableOccurrence;
import tv.esporx.domain.Occurrence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
    public Long saveOrUpdate(Occurrence occurrence, List<Long> channelIds, Long eventId) {
        boolean alreadyPersisted = (occurrence.getId() != null);
        entityManager.persist(occurrence);
        long occurrenceId = occurrence.getId();

        executeEventInsert(occurrenceId, eventId, alreadyPersisted);
        executeBatchChannelInsert(occurrenceId, channelIds);
        return occurrenceId;
    }

    private void executeEventInsert(long occurrenceId, Long eventId, boolean alreadyPersisted) {
        if(!alreadyPersisted) {
            jdbcTemplate.update(
                    "INSERT INTO event_occurrence(event_id, occurrence_id) VALUES(?,?)",
                    new Long[] {eventId, occurrenceId}
            );
        }
    }

    private void executeBatchChannelInsert(final long occurrenceId, final List<Long> channelIds) {
        jdbcTemplate.update("DELETE FROM occurrences_channels WHERE occurrence_id = ?", occurrenceId);
        jdbcTemplate.batchUpdate("INSERT INTO occurrences_channels(occurrence_id, channel_id) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int id) throws SQLException {
                ps.setLong(1, occurrenceId);
                ps.setLong(2, channelIds.get(id));
            }

            @Override
            public int getBatchSize() {
                return channelIds.size();
            }
        });
    }

}
