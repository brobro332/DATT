package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.dto.PlaceMasterSearchResponse;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceMasterService {
    private final PlaceMasterRepository placeMasterRepository;

    public List<PlaceMasterSearchResponse> searchByKeyword(String keyword) {
        return placeMasterRepository.findByBizesNmContaining(keyword)
            .stream()
            .map(PlaceMasterSearchResponse::from)
            .toList();
    }
}