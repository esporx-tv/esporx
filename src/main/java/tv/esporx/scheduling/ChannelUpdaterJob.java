package tv.esporx.scheduling;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tv.esporx.collections.functions.ByVideoProviderChannelIndexer;
import tv.esporx.domain.Channel;
import tv.esporx.domain.VideoProvider;
import tv.esporx.framework.collection.Tuple;
import tv.esporx.repositories.ChannelRepository;
import tv.esporx.scheduling.wrappers.DailymotionChannelResponse;
import tv.esporx.scheduling.wrappers.TwitchTVChannelResponse;
import tv.esporx.scheduling.wrappers.YoutubeChannelResponse;

import javax.annotation.PostConstruct;
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
@Lazy(false)
public class ChannelUpdaterJob {
	
    private static final String UPDATE_CHANNELS_QUERY = "UPDATE channels SET viewer_count = ?, viewer_count_timestamp = ? WHERE video_url = ?";
	private static final Logger LOGGER = getLogger(ChannelUpdaterJob.class);
    private final Map<String, Class<? extends ChannelResponse[]>> apiEndpointBatchResponseClassMapping;
    private final Map<String, Class<? extends ChannelResponse>> apiEndpointSingleResponseClassMapping;

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public ChannelUpdaterJob() {
		apiEndpointBatchResponseClassMapping = new HashMap<String, Class<? extends ChannelResponse[]>>();
    	apiEndpointBatchResponseClassMapping.put("http://api.justin.tv/api/stream/list.json?channel={ID}", TwitchTVChannelResponse[].class);
        apiEndpointSingleResponseClassMapping = new HashMap<String, Class<? extends ChannelResponse>>();
        apiEndpointSingleResponseClassMapping.put("https://gdata.youtube.com/feeds/api/videos/{ID}?v=2&alt=json", YoutubeChannelResponse.class);
        apiEndpointSingleResponseClassMapping.put("https://api.dailymotion.com/video/{ID}?fields=id,views_last_hour", DailymotionChannelResponse.class);
	}

    @PostConstruct
    public void setJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    @Scheduled(cron = "0/30 * * * * *")
    public void execute() {
        for (Map.Entry<VideoProvider, Collection<Channel>> channelsWithProvider : fetchAllChannels(channelRepository).entrySet()) {
            VideoProvider provider = channelsWithProvider.getKey();
            List<Tuple<String,String>> titleUrlCouples = channelName_URL(provider, channelsWithProvider.getValue());
            List<String> channelNames = getChannelNames(titleUrlCouples);
            if(!channelNames.isEmpty()) {
            	ChannelResponse[] responses = getChannelResponsesPerProvider(provider, channelNames);
            	if (responses.length > 0) {
                	doBatchUpdate(jdbcTemplate, responses, titleUrlCouples);
            	}
            }
        }
    }

    private ChannelResponse[] getChannelResponsesPerProvider(VideoProvider provider, List<String> channelNames) {
        String endpointTemplate = provider.getEndpoint();
        if (isBatchProviderSupported(provider)) {
            return fetchBatchResponses(provider, channelNames, endpointTemplate);
		}
        else if (isSingleProviderSupported(provider)) {
            return fetchSingleResponses(provider, channelNames, endpointTemplate);
        }
        return new ChannelResponse[] {};
    }

    private ChannelResponse[] fetchBatchResponses(VideoProvider provider, List<String> channelNames, String endpointTemplate) {
        String channels = Joiner.on(",").skipNulls().join(channelNames);
        LOGGER.info("About to query : " + endpointTemplate + " with: " + channels);
        return restTemplate().getForObject(endpointTemplate, apiEndpointBatchResponseClassMapping.get(provider.getEndpoint()), channels);
    }

    private ChannelResponse[] fetchSingleResponses(VideoProvider provider, List<String> channelNames, String endpointTemplate) {
        ChannelResponse[] responses = new ChannelResponse[channelNames.size()];
        int index = 0;
        for (String channel : channelNames) {
            LOGGER.info("About to query : " + endpointTemplate + " with: " + channel);
            responses[index++] = restTemplate().getForObject(endpointTemplate, apiEndpointSingleResponseClassMapping.get(provider.getEndpoint()), channel);
        }
        return responses;
    }

    private boolean isBatchProviderSupported(VideoProvider provider) {
        return apiEndpointBatchResponseClassMapping.get(provider.getEndpoint()) != null;
    }

    private boolean isSingleProviderSupported(VideoProvider provider) {
        return apiEndpointSingleResponseClassMapping.get(provider.getEndpoint()) != null;
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
                    Tuple<String, Integer> tuple = urlAndViewerCountTuples.get(i);
                    ps.setInt(1, tuple.getRight());
					ps.setTimestamp(2, timestamp);
					ps.setString(3, tuple.getLeft());
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
                String channelName = provider.extractChannelName(videoUrl);
                if ("LOWERCASE".equals(provider.getCaseMode())) {
                    channelName = channelName.toLowerCase();
                }
                else if ("UPPERCASE".equals(provider.getCaseMode())) {
                    channelName = channelName.toUpperCase();
                }
				return new Tuple<String, String>(channelName, videoUrl);
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

    private List<String> getChannelNames(List<Tuple<String, String>> urlTitleCouples) {
        return Lists.transform(urlTitleCouples, new Function<Tuple<String, String>, String>() {
            @Override
            public String apply(Tuple<String, String> couple) {
                return couple.getLeft();
            }
        });
    }

    private Map<VideoProvider, Collection<Channel>> fetchAllChannels(ChannelRepository channelRepository) {
        Iterable<Channel> channels = channelRepository.findAllGroupedByProvider();
        return index(channels, new ByVideoProviderChannelIndexer()).asMap();
    }
}
