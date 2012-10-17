package tv.esporx.dao;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import tv.esporx.domain.Broadcast;

public interface PersistenceCapableBroadcast {

	public List<Broadcast> findTimeLine(final DateTime startDate, final DateTime maxDate);

}
