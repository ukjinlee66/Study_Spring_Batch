package com.exambatch.SpringBatchTutorial.core.scheduler;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleScheduler {

    private final Job helloWorldJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "*/10 * * * * *")
    public void helloWorldJobRun() throws Exception{
        JobParameters jobParameters = new JobParameters(
            Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis()))
        );
        jobLauncher.run(helloWorldJob, jobParameters);
    }
}
