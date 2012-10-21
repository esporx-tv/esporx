package tv.esporx.scheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ChannelUpdaterJob implements Job {

    private final String endpoint;

    public ChannelUpdaterJob(String resolvedEndpoint) {
        endpoint = resolvedEndpoint;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("caca");
    }
}
