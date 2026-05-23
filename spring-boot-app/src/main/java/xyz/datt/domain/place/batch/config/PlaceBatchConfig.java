package xyz.datt.domain.place.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.datt.domain.place.batch.client.PlacePublicDataClient;
import xyz.datt.domain.place.batch.processor.PlaceItemProcessor;
import xyz.datt.domain.place.batch.reader.PlaceApiItemReader;
import xyz.datt.domain.place.batch.writer.PlaceItemWriter;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

@Configuration
public class PlaceBatchConfig {
    @Bean
    public PlaceApiItemReader placeApiItemReader(PlacePublicDataClient placePublicDataClient) {
        return new PlaceApiItemReader(placePublicDataClient);
    }

    @Bean
    public PlaceItemProcessor placeItemProcessor() {
        return new PlaceItemProcessor();
    }

    @Bean
    public PlaceItemWriter placeItemWriter(PlaceMasterRepository placeMasterRepository) {
        return new PlaceItemWriter(placeMasterRepository);
    }
}