package tv.esporx.scheduling;

import org.quartz.*;

public class VideoChannelUpdaterJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("caca");
    }
}
