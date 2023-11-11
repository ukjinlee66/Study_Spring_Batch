package com.exambatch.SpringBatchTutorial.job.JobListener;

import javax.batch.api.listener.JobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@SuppressWarnings("SpringJavaAutowiringInspection")
@RequiredArgsConstructor
public class JobListenerConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job JobListenerJob(Step JobListenerStep) {
        return jobBuilderFactory.get("JobListenerJob")
            .incrementer(new RunIdIncrementer()) // JobParameters의 ID를 자동으로 생성해주는 incrementer
            .listener(new JobLoggerListener())
            .start(JobListenerStep)
            .build();
    }

    @JobScope
    @Bean
    public Step JobListenerStep(Tasklet JobListenerTasklet) {
        return stepBuilderFactory.get("JobListenerStep")
            .tasklet(JobListenerTasklet)
            .listener(new JobLoggerListener())
            .build();
    }

    @StepScope
    @Bean
    public Tasklet JobListenerTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                throws Exception {
                log.info("JobListener");
                return RepeatStatus.FINISHED;
            }
        };
    }
}

