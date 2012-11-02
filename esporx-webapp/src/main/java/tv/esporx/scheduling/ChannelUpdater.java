package tv.esporx.scheduling;

import org.quartz.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import tv.esporx.repositories.ChannelRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Lazy(false)
public class ChannelUpdater {
	
	private static final Logger LOGGER = getLogger(ChannelUpdater.class);
	private final SchedulerFactoryBean schedulerFactory;
	private final ChannelRepository channelRepository;
	private final DataSource dataSource;

    @Autowired
    public ChannelUpdater(SchedulerFactoryBean schedulerFactory,
                          ChannelRepository channelRepository,
                          DataSource dataSource) {

        this.schedulerFactory = schedulerFactory;
        this.schedulerFactory.setAutoStartup(true);
        this.channelRepository = channelRepository;
        this.dataSource = dataSource;
    }

	@PostConstruct
	public void scheduleUpdateExecution() throws SchedulerException {
        Trigger trigger = trigger();
        schedulerFactory.getScheduler().scheduleJob(job(), trigger);
		LOGGER.info(">>> Update job start time: " + trigger.getStartTime());
	}

    private JobDetail job() {
        return newJob(ChannelUpdaterJob.class)                                      //
                    .usingJobData(createJobDataMap())                               //
                    .withIdentity("ChannelUpdaterJob")                              //
                    .build();
    }

    private Trigger trigger() {
        return newTrigger()                                                         //
                    .withIdentity("ChannelUpdaterJob")                              //
                    .startNow()                                                     //
                    .withSchedule(                                                  //
                            SimpleScheduleBuilder                                   //
                                    .simpleSchedule()                               //
                                    .withIntervalInSeconds(30)                      //
                                    .repeatForever())                               //
                    .build();
    }

    //FIXME: this is very (ha|su)cky :/
	private JobDataMap createJobDataMap() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("dataSource", dataSource);
		jobDataMap.put("channelDao", channelRepository);
		return jobDataMap;
	}

}
