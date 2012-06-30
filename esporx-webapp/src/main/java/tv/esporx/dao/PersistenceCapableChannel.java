package tv.esporx.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;

import tv.esporx.domain.Channel;

public interface PersistenceCapableChannel {

	void delete(Channel channel);

	Channel findById(long i);

	Channel findByTitle(String string);

	List<Channel> findMostViewed();

	void saveOrUpdate(Channel channel);

	void setEntityManager(EntityManager entityManager);

	List<Channel> findAll();

	List<Channel> findTimeLine(DateTime from, DateTime to);

}
