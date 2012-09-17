package tv.esporx.scheduling;

import org.quartz.*;

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
