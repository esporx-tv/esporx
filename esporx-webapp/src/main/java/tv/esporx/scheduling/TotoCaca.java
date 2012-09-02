package tv.esporx.scheduling;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class TotoCaca {
    public static void main(String... args) throws SchedulerException {

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();

        JobDetail job = newJob(VideoChannelUpdaterJob.class)
                .withIdentity("videoJobKilledTheRadioStars", "prout")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("videoJobKilledTheRadioStars", "prout")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();


        scheduler.scheduleJob(job, trigger);
    }
}
