package com.exambatch.SpringBatchTutorial.job.DbDateReadWrite;

import com.exambatch.SpringBatchTutorial.core.domain.accounts.Accounts;
import com.exambatch.SpringBatchTutorial.core.domain.accounts.AccountsRepository;
import com.exambatch.SpringBatchTutorial.core.domain.orders.Orders;
import com.exambatch.SpringBatchTutorial.core.domain.orders.OrdersRepository;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    private final OrdersRepository ordersRepository;
    private final AccountsRepository accountsRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
            .incrementer(new RunIdIncrementer()) // JobParameters의 ID를 자동으로 생성해주는 incrementer
            .start(trMigrationStep)
            .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader trOrdersReader, ItemProcessor trOrderProcessor,
        ItemWriter trOrdersWriter) {
        return stepBuilderFactory.get("trMigrationStep")
            .<Orders, Accounts>chunk(5)
            .reader(trOrdersReader)
            .processor(trOrderProcessor)
            .writer(trOrdersWriter)
            .build();
    }

//    @StepScope
//    @Bean
//    public RepositoryItemWriter<Accounts> trOrdersWriter(){
//        return new RepositoryItemWriterBuilder<Accounts>()
//            .repository(accountsRepository)
//            .methodName("save")
//            .build();
//    }

    @StepScope
    @Bean
    // 비즈니스 로직을 추가 해야 할 경우
    public ItemWriter<Accounts> trOrdersWriter() {
        return new ItemWriter<Accounts>() {
            @Override
            public void write(List<? extends Accounts> items) throws Exception {
                items.stream().forEach(accounts -> {
                    accountsRepository.save(accounts);
                });
            }
        };
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor() {
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(Orders item) throws Exception {
                return Accounts.builder()
                    .orderItem(item.getOrderItem())
                    .price(item.getPrice())
                    .orderDate(item.getOrderDate())
                    .accountDate(new Date())
                    .build();
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
            .name("trOrdersReader")
            .repository(ordersRepository)
            .methodName("findAll")
            .pageSize(5)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    }

}
