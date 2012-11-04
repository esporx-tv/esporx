package tv.esporx.scheduling;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.quartz.Job;
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
import tv.esporx.collections.ByVideoProviderChannelIndexer;
import tv.esporx.domain.Channel;
import tv.esporx.domain.VideoProvider;
import tv.esporx.framework.collection.Tuple;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.scheduling.wrappers.TwitchTVChannelResponse;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Multimaps.index;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ChannelUpdaterJob implements Job {
	
    private static final String UPDATE_CHANNELS_QUERY = "UPDATE channels SET viewer_count = ?, viewer_count_timestamp = ? WHERE video_url = ?";
	private static final Logger LOGGER = getLogger(ChannelUpdater.class);
    private final Map<String, Class<? extends ChannelResponse[]>> apiEndpointResponseClassMapping;
    
	public ChannelUpdaterJob() {
		apiEndpointResponseClassMapping = new HashMap<String, Class<? extends ChannelResponse[]>>();
    	apiEndpointResponseClassMapping.put("http://api.justin.tv/api/stream/list.json?channel={ID}", TwitchTVChannelResponse[].class);
	}
	
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JdbcTemplate jdbcTemplate = createJdbcTemplate(jobExecutionContext);
        ChannelRepository channelRepository = retrieveChannelRepository(jobExecutionContext);

        for (Map.Entry<VideoProvider, Collection<Channel>> channelsWithProvider : fetchAllChannels(channelRepository).entrySet()) {
            VideoProvider provider = channelsWithProvider.getKey();
            List<Tuple<String,String>> titleUrlCouples = channelName_URL(provider, channelsWithProvider.getValue());
            String channelNames = getCombinedChannelNames(titleUrlCouples);

            if(!channelNames.isEmpty()) {
            	ChannelResponse[] responses = getChannelResponsesPerProvider(provider, channelNames);
            	if (responses.length > 0) {
                	doBatchUpdate(jdbcTemplate, responses, titleUrlCouples);
            	}
            }
        }
    }

    private ChannelRepository retrieveChannelRepository(JobExecutionContext jobExecutionContext) {
        return (ChannelRepository) jobExecutionContext.getMergedJobDataMap().get("channelDao");
    }

    private JdbcTemplate createJdbcTemplate(JobExecutionContext jobExecutionContext) {
        return new JdbcTemplate((DataSource) jobExecutionContext.getMergedJobDataMap().get("dataSource"));
    }


    private ChannelResponse[] getChannelResponsesPerProvider(VideoProvider provider, String channelNames) {
    	ChannelResponse[] responses = new ChannelResponse[] {};

		if(isProviderSupported(provider)) {
            String endpointTemplate = provider.getEndpoint();
	        LOGGER.info("About to query : " + endpointTemplate + " with: " + channelNames);

            responses = restTemplate().getForObject(endpointTemplate, apiEndpointResponseClassMapping.get(provider.getEndpoint()), channelNames);
		}

        return responses;
    }

    private boolean isProviderSupported(VideoProvider provider) {
        return apiEndpointResponseClassMapping.get(provider.getEndpoint()) != null;
    }

    private RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        template.setMessageConverters(httpConverters(template.getMessageConverters()));
        return template;
    }

    private List<HttpMessageConverter<?>> httpConverters(List<HttpMessageConverter<?>> messageConverters) {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(messageConverters);

        MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(jsonConverter);

        return converters;
    }

    private void doBatchUpdate(JdbcTemplate jdbcTemplate, ChannelResponse[] responses, List<Tuple<String,String>> titleUrlCouples) {
    	final Timestamp timestamp = new Timestamp(DateTime.now().getMillis());

    	final List<Tuple<String,Integer>> urlAndViewerCountTuples = URL_viewerCount(asList(responses), titleUrlCouples);
        jdbcTemplate.batchUpdate(UPDATE_CHANNELS_QUERY,
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

    /**
     * Returns Channel names (LEFT) together with their URL (RIGHT)
     */
    private List<Tuple<String, String>> channelName_URL(final VideoProvider provider, final Collection<Channel> channels) {
    	return newLinkedList(transform(channels, new Function<Channel, Tuple<String, String>>() {
            @Override
            public Tuple<String, String> apply(Channel channel) {
                String videoUrl = channel.getVideoUrl();
				return new Tuple<String, String>(provider.extractChannelName(videoUrl), videoUrl);
            }
        }));
    }

    /**
     * Returns Video URLs (LEFT) together with their viewer count (RIGHT)
     */
    private List<Tuple<String, Integer>> URL_viewerCount(final List<ChannelResponse> responses, final List<Tuple<String, String>> titleUrlCouples) {
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

    private Map<VideoProvider, Collection<Channel>> fetchAllChannels(ChannelRepository channelRepository) {
        Iterable<Channel> channels = channelRepository.findAllGroupedByProvider();
        return index(channels, new ByVideoProviderChannelIndexer()).asMap();
    }
}
