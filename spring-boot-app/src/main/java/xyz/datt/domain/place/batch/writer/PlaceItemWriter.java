package xyz.datt.domain.place.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

@RequiredArgsConstructor
public class PlaceItemWriter implements ItemWriter<PlaceMaster> {
    private final PlaceMasterRepository placeMasterRepository;

    @Override
    public void write(Chunk<? extends PlaceMaster> chunk) {
        for (PlaceMaster placeMaster : chunk.getItems()) {
            upsert(placeMaster);
        }
    }

    private void upsert(PlaceMaster placeMaster) {
        placeMasterRepository.findByBizesId(placeMaster.getBizesId())
            .ifPresentOrElse(
                existingPlaceMaster -> existingPlaceMaster.updateFrom(placeMaster),
                () -> placeMasterRepository.save(placeMaster)
            );
    }
}