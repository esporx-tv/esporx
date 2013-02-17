package tv.esporx.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.Channel;

import java.util.List;

public interface ChannelRepository extends CrudRepository<Channel,Long> {

	Channel findByTitle(String title);

    @Query( "FROM Channel channel WHERE channel.viewerCount IS NOT NULL " +                  //
            "ORDER BY channel.viewerCount DESC")
    List<Channel> findMostViewed();

    @Query( "FROM Channel channel " +                   //
            "WHERE event IS NOT NULL")
    List<Channel> findTimeLine(DateTime from, DateTime to);

    @Query( "FROM Channel channel " +                   //
            "LEFT JOIN FETCH channel.videoProvider " +  //
            "ORDER BY channel.title ASC")
    List<Channel> findAllWithFetchedProviders();

    @Query( "FROM Channel channel " +                   //
            "WHERE channel.videoProvider IS NOT NULL " +//
            "ORDER BY channel.videoProvider")
    List<Channel> findAllGroupedByProvider();

}
