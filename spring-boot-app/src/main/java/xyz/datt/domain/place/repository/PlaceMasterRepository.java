package xyz.datt.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.entity.PlaceMaster;

import java.util.List;
import java.util.Optional;

public interface PlaceMasterRepository extends JpaRepository<PlaceMaster, Long>, PlaceQueryRepository {
    boolean existsByBizesId(String bizesId);
    Optional<PlaceMaster> findByBizesId(String bizesId);
    List<PlaceMaster> findByBizesNmContaining(String keyword);
    List<PlaceMaster> findByCtprvnNmAndSignguNm(String ctprvnNm, String signguNm);
    List<PlaceMaster> findByIndsLclsNm(String indsLclsNm);
    List<PlaceMaster> findByIndsMclsNm(String indsMclsNm);
    List<PlaceMaster> findByIndsSclsNm(String indsSclsNm);
}