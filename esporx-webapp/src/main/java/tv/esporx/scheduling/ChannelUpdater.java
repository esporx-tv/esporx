package tv.esporx.scheduling;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tv.esporx.domain.Channel;
import tv.esporx.domain.VideoProvider;
import tv.esporx.domain.remote.TwitchTVChannelResponse;
import tv.esporx.services.ChannelByVideoProviderIndexer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Multimaps.index;

@Component
public class ChannelUpdater {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChannelUpdater(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public static void main(String... args) {
        //ApplicationContext applicationContext = new FileSystemXmlApplicationContext("file:**/applicationContext.xml");
        //applicationContext.getBean(ChannelUpdater.class);
        //TODO: rewrite this as a proper integration test !!

    }

    @PostConstruct
    public void executeChannelUpdates() {
        Map<VideoProvider, Collection<Channel>> channelsPerProvider = fetchAllChannels();
        for (Map.Entry<VideoProvider, Collection<Channel>> channelsWithProvider : channelsPerProvider.entrySet()) {
            VideoProvider provider = channelsWithProvider.getKey();
            String channelNames = getCombinedChannelNames(provider, channelsWithProvider.getValue());
            if(!channelNames.isEmpty()) {
                RestTemplate template = new RestTemplate();

                List<HttpMessageConverter<?>> messageConverters = template.getMessageConverters();
                List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(messageConverters);

                MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
                converters.add(new FormHttpMessageConverter());
                converters.add(new StringHttpMessageConverter());
                converters.add(jsonConverter);
                template.setMessageConverters(converters);

                TwitchTVChannelResponse[] twitchTVChannelResponse;

                String endpointTemplate = provider.getEndpoint();
                System.out.println("About to query : " + endpointTemplate + " with: "+channelNames);

                twitchTVChannelResponse = template.getForObject(endpointTemplate, TwitchTVChannelResponse[].class, channelNames);

                for (TwitchTVChannelResponse response : twitchTVChannelResponse) {
                    System.out.println(response.getViewerCount());
                    System.out.println(response.getChannelName().getChannelName());
                }
            }
        }
    }

    private String getCombinedChannelNames(final VideoProvider provider, final Collection<Channel> channels) {
        return provider == null ? "" : Joiner.on(',').skipNulls().join(
            transform(channels, new Function<Channel, String>() {
                @Override
                public String apply(Channel channel) {
                    return provider.extractChannelName(channel.getVideoUrl());
                }
            })
        );
    }

    private Map<VideoProvider, Collection<Channel>> fetchAllChannels() {
        List<Channel> channels = jdbcTemplate.query("SELECT * FROM video_providers GROUP BY provider", new BeanPropertyRowMapper<Channel>(Channel.class));
        return index(channels, new ChannelByVideoProviderIndexer()).asMap();
    }
//
//    public static void main(String... args) throws SchedulerException {
//
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//
//        JobDetail job = newJob(ChannelUpdaterJob.class)
//                .withIdentity("videoJobKilledTheRadioStars", "prout")
//                .build();
//
//        Trigger trigger = newTrigger()
//                .withIdentity("videoJobKilledTheRadioStars", "prout")
//                .startNow()
//                .withSchedule(simpleSchedule()
//                        .withIntervalInSeconds(1)
//                        .repeatForever())
//                .build();
//
//
//        scheduler.scheduleJob(job, trigger);
//    }
}
