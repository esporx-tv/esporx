package tv.esporx.scheduling;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Multimaps.index;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import tv.esporx.dao.PersistenceCapableChannel;
import tv.esporx.domain.Channel;
import tv.esporx.domain.VideoProvider;
import tv.esporx.domain.remote.ChannelResponse;
import tv.esporx.domain.remote.TwitchTVChannelResponse;
import tv.esporx.framework.Tuple;
import tv.esporx.services.ChannelByVideoProviderIndexer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@Component
public class ChannelUpdaterJob implements Job {
	
	private static Logger logger = getLogger(ChannelUpdater.class);

	private HashMap<String, Class<? extends ChannelResponse[]>> matchEndPointWithClass;
    
	public ChannelUpdaterJob() {
		matchEndPointWithClass = new HashMap<String, Class<? extends ChannelResponse[]>>();
    	matchEndPointWithClass.put("http://api.justin.tv/api/stream/list.json?channel={ID}", TwitchTVChannelResponse[].class);
	}
	
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
    	JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) mergedJobDataMap.get("dataSource"));
    	PersistenceCapableChannel channelDao = (PersistenceCapableChannel) mergedJobDataMap.get("channelDao");
        Map<VideoProvider, Collection<Channel>> channelsPerProvider = fetchAllChannels(channelDao);
        for (Map.Entry<VideoProvider, Collection<Channel>> channelsWithProvider : channelsPerProvider.entrySet()) {
            VideoProvider provider = channelsWithProvider.getKey();
            List<Tuple<String,String>> titleUrlCouples = getTitleUrlCouples(provider, channelsWithProvider.getValue());
            String channelNames = getCombinedChannelNames(titleUrlCouples);
            if(!channelNames.isEmpty()) {
            	ChannelResponse[] responses = getChannelResponsesPerProvider(provider, channelNames);
            	if (responses.length > 0) {
                	doBatchUpdate(jdbcTemplate, responses, titleUrlCouples);
            	}
            }
        }
    }
    
    private void doBatchUpdate(JdbcTemplate jdbcTemplate, ChannelResponse[] responses, List<Tuple<String,String>> titleUrlCouples) {
    	final Timestamp timestamp = new Timestamp(DateTime.now().getMillis());
    	
    	final List<Tuple<String,Integer>> urlAndViewerCountTuples = getVideoUrlAndViewerCountTuples(asList(responses), titleUrlCouples);
        jdbcTemplate.batchUpdate("UPDATE channels SET viewer_count = ?, viewer_count_timestamp = ? WHERE video_url = ?",
        	new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, urlAndViewerCountTuples.get(i).getRight());
					ps.setTimestamp(2, timestamp);
					ps.setString(3, urlAndViewerCountTuples.get(i).getLeft());
				}
				@Override
				public int getBatchSize() {
					return urlAndViewerCountTuples.size();
				}
			});
    }
    
    private ChannelResponse[] getChannelResponsesPerProvider(VideoProvider provider, String channelNames) {
    	ChannelResponse[] responses = new ChannelResponse[]{};
    	RestTemplate template = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = template.getMessageConverters();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(messageConverters);

        MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(jsonConverter);
        template.setMessageConverters(converters);
        
        Class<? extends ChannelResponse[]> channelResponseClass = matchEndPointWithClass.get(provider.getEndpoint());

		if(channelResponseClass != null) {
	        String endpointTemplate = provider.getEndpoint();
	        logger.info("About to query : " + endpointTemplate + " with: "+channelNames);
	
	        responses = template.getForObject(endpointTemplate, channelResponseClass, channelNames);
		}
		
        return responses;
    }
    
    private List<Tuple<String, String>> getTitleUrlCouples(final VideoProvider provider, final Collection<Channel> channels) {
    	return newLinkedList(transform(channels, new Function<Channel, Tuple<String, String>>() {
            @Override
            public Tuple<String, String> apply(Channel channel) {
                String videoUrl = channel.getVideoUrl();
				return new Tuple<String, String>(provider.extractChannelName(videoUrl), videoUrl);
            }
        }));
    }
    
    private List<Tuple<String, Integer>> getVideoUrlAndViewerCountTuples(final List<ChannelResponse> responses,final List<Tuple<String, String>> titleUrlCouples) {
		return Lists.transform(responses, new Function<ChannelResponse, Tuple<String, Integer>>(){

			@Override
			public Tuple<String, Integer> apply(ChannelResponse response) {
				return new Tuple<String, Integer>(//
						Tuple.<String, String>findFirstRight(titleUrlCouples, response.getChannelName()), //
						parseInt(response.getViewerCount())
				);
			}
		
		});
    	
    }

    private String getCombinedChannelNames(List<Tuple<String, String>> urlTitleCouples) {
        return Joiner.on(',').skipNulls().join(
            transform(urlTitleCouples, new Function<Tuple<String, String>, String>() {
                @Override
                public String apply(Tuple<String, String> couple) {
                    return couple.getLeft();
                }
            })
        );
    }

    private Map<VideoProvider, Collection<Channel>> fetchAllChannels(PersistenceCapableChannel channelDao) {
        List<Channel> channels = channelDao.findAllGroupByProvider();
        return index(channels, new ChannelByVideoProviderIndexer()).asMap();
    }
}
