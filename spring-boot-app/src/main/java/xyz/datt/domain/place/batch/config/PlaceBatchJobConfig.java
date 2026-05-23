package xyz.datt.domain.place.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import xyz.datt.domain.place.batch.dto.PlacePublicDataItem;
import xyz.datt.domain.place.batch.processor.PlaceItemProcessor;
import xyz.datt.domain.place.batch.reader.PlaceApiItemReader;
import xyz.datt.domain.place.batch.writer.PlaceItemWriter;
import xyz.datt.domain.place.entity.PlaceMaster;

@Configuration
@RequiredArgsConstructor
public class PlaceBatchJobConfig {
    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job placeSyncJob(JobRepository jobRepository, Step placeSyncStep) {
        return new JobBuilder("placeSyncJob", jobRepository)
            .start(placeSyncStep)
            .build();
    }

    @Bean
    public Step placeSyncStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        PlaceApiItemReader placeApiItemReader,
        PlaceItemProcessor placeItemProcessor,
        PlaceItemWriter placeItemWriter
    ) {
        return new StepBuilder("placeSyncStep", jobRepository)
            .<PlacePublicDataItem, PlaceMaster>chunk(CHUNK_SIZE)
            .reader(placeApiItemReader)
            .processor(placeItemProcessor)
            .writer(placeItemWriter)
            .transactionManager(transactionManager)
            .build();
    }
}