package com.exambatch.SpringBatchTutorial.job.Hello;

import com.exambatch.SpringBatchTutorial.SpringBatchTestConfig;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, HelloWorldJobConfig.class})
public class HelloWorldJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void success() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
    }
}