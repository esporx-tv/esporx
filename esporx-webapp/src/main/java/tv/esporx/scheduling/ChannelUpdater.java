package tv.esporx.scheduling;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.slf4j.LoggerFactory.getLogger;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import tv.esporx.dao.PersistenceCapableChannel;

@Component
@Lazy(false)
public class ChannelUpdater {
	
	private static Logger logger = getLogger(ChannelUpdater.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PersistenceCapableChannel channelDao;

	@PostConstruct
	public void scheduleUpdateExecution() throws SchedulerException {
		schedulerFactory.setAutoStartup(true);
		Scheduler scheduler = schedulerFactory.getScheduler();

		JobDetail job = newJob(ChannelUpdaterJob.class)
				.usingJobData(createJobDataMap()) //
				.withIdentity("ChannelUpdaterJob").build();

		Trigger trigger = newTrigger() //
				.withIdentity("ChannelUpdaterJob") //
				.startNow() //
				.withSchedule( //
						SimpleScheduleBuilder.simpleSchedule() //
								.withIntervalInSeconds(30).repeatForever()) //
				.build();
		scheduler.scheduleJob(job, trigger);

		logger.info(">>> Update job start time: " + trigger.getStartTime());
	}

	private JobDataMap createJobDataMap() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("dataSource", dataSource);
		jobDataMap.put("channelDao", channelDao);
		return jobDataMap;
	}

}
