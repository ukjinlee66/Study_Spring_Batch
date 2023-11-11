package com.exambatch.SpringBatchTutorial.job.JobListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String START_MESSAGE = "%s is beginning execution";
    private static String END_MESSAGE = "%s has completed with the status(Status: %s)";
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(String.format(START_MESSAGE, jobExecution.getJobInstance().getJobName()));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(String.format(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus()));
    }
}
