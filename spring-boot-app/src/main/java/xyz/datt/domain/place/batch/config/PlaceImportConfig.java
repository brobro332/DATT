package xyz.datt.domain.place.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.datt.domain.place.batch.processor.PlaceItemProcessor;
import xyz.datt.domain.place.batch.reader.PlaceApiItemReader;
import xyz.datt.domain.place.dto.PlaceMasterResponseDto;
import xyz.datt.domain.place.entity.PlaceMaster;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlaceImportConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final PlaceItemProcessor placeItemProcessor;
    private final ItemWriter<PlaceMaster> placeItemWriter;
    private final WebClient dataGoWebClient;

    @Bean
    public Job placeImportJob() {
        return new JobBuilder("placeImportJob", jobRepository)
            .start(placeImportStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step placeImportStep() {
        return new StepBuilder("placeImportStep", jobRepository)
            .<PlaceMasterResponseDto, PlaceMaster>chunk(1000)
            .transactionManager(platformTransactionManager)
            .reader(storeApiReader())
            .processor(placeItemProcessor)
            .writer(placeItemWriter)
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<PlaceMasterResponseDto> storeApiReader() {
        return new PlaceApiItemReader(dataGoWebClient);
    }
}
