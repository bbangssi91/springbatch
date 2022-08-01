package io.study.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class StepBuilderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job BatchJob(){
        return jobBuilderFactory.get("batchJob")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .tasklet((stepContribution, chunkContext) -> {
                System.out.println("step1");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<String, String>chunk(3)
                .reader(new ListItemReader<>(Arrays.asList(
                        "item1",
                        "item2",
                        "item3",
                        "item4",
                        "item5"
                )))
                .processor((ItemProcessor<String, String>) String::toUpperCase)
                .writer(list -> list.forEach(System.out::println))
                .build();
    }

}
