package xyz.datt.domain.place.batch.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemReader;
import xyz.datt.domain.place.batch.client.PlacePublicDataClient;
import xyz.datt.domain.place.batch.dto.PlacePublicDataItem;
import xyz.datt.domain.place.entity.PlaceIndustryCategory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class PlaceApiItemReader implements ItemReader<PlacePublicDataItem> {
    private static final String DIV_ID = "indsMclsCd";
    private final PlacePublicDataClient placePublicDataClient;
    private final List<PlaceIndustryCategory> categories = List.of(PlaceIndustryCategory.values());
    private int categoryIndex = 0;
    private int pageNo = 1;
    private Iterator<PlacePublicDataItem> currentIterator = Collections.emptyIterator();

    @Override
    public PlacePublicDataItem read() {
        while (true) {
            if (currentIterator.hasNext()) return currentIterator.next();
            if (categoryIndex >= categories.size()) return null;

            PlaceIndustryCategory category = categories.get(categoryIndex);

            List<PlacePublicDataItem> items = placePublicDataClient.fetchPlaces(
                DIV_ID,
                category.getCode(),
                pageNo
            );

            if (items.isEmpty()) {
                categoryIndex++;
                pageNo = 1;
                continue;
            }

            pageNo++;
            currentIterator = items.iterator();
        }
    }
}