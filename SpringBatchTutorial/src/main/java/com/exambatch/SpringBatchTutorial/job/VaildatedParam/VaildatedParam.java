package com.exambatch.SpringBatchTutorial.job.VaildatedParam;

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
public class VaildatedParam {

    // csv 파일 읽기 -> 필요 없음.
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job VaildatedParamJob(Step VaildatedParamStep) {
        return jobBuilderFactory.get("VaildatedParamJob")
            .incrementer(new RunIdIncrementer()) // JobParameters의 ID를 자동으로 생성해주는 incrementer
            .start(VaildatedParamStep)
            .build();
    }

    @JobScope
    @Bean
    public Step VaildatedParamStep(Tasklet VaildatedParamTasklet) {
        return stepBuilderFactory.get("VaildatedParamStep")
            .tasklet(VaildatedParamTasklet)
            .build();
    }

    @StepScope
    @Bean
    public Tasklet VaildatedParamTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                throws Exception {
                log.info("HelloWorld Spring Batch! Validated Param");
                return RepeatStatus.FINISHED;
            }
        };
    }
}

