package tv.esporx.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import tv.esporx.domain.Occurrence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * This a custom hook to built-in JPA methods provided by SpringData.
 * This becomes automagically managed by Spring container, no need to expose it directly in the context.
 * DO NOT USE THIS DIRECTLY
 */
class OccurrenceRepositoryImpl implements OccurrenceRepositoryCustom {

    private static final String DELETE_OCCURRENCE_CHANNEL_GIVEN_OCCURRENCE = "DELETE FROM occurrences_channels WHERE occurrence_id = ?";
    private static final String INSERT_OCCURRENCE_CHANNEL = "INSERT INTO occurrences_channels(occurrence_id, channel_id) VALUES (?, ?)";
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OccurrenceRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long saveWithAssociations(final Occurrence occurrence, final List<Long> channelIds) {
        entityManager.persist(occurrence);
        long occurrenceId = occurrence.getId();

        executeBatchChannelInsert(occurrenceId, channelIds);
        return occurrenceId;
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
