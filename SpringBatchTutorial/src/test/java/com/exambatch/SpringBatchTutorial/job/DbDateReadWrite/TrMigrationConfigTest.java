package com.exambatch.SpringBatchTutorial.job.DbDateReadWrite;

import com.exambatch.SpringBatchTutorial.SpringBatchTestConfig;
import com.exambatch.SpringBatchTutorial.core.domain.accounts.AccountsRepository;
import com.exambatch.SpringBatchTutorial.core.domain.orders.Orders;
import com.exambatch.SpringBatchTutorial.core.domain.orders.OrdersRepository;
import java.util.Date;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
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
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class})
public class TrMigrationConfigTest {
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    AccountsRepository accountsRepository;

    @AfterEach
    public void cleanUpEach() {
        ordersRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test()
    public void success_noData() throws Exception {
        // given
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        // then

        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);

        Assertions.assertEquals(0, accountsRepository.count());

    }

    @Test()
    public void success_existData() throws Exception {
        // given
        Orders orders1 = new Orders(null, "kakao gift", 15000, new Date());
        Orders orders2 = new Orders(null, "naver gift", 15000, new Date());

        ordersRepository.save(orders1);
        ordersRepository.save(orders2);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then

        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountsRepository.count());

    }


}