package xyz.datt.domain.place.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.datt.domain.place.entity.PlaceMaster;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class PlaceItemWriterConfig {
    private final DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<PlaceMaster> placeItemWriter() {
        return new JdbcBatchItemWriterBuilder<PlaceMaster>()
            .dataSource(dataSource)
            .sql("""
                INSERT INTO placeMaster (
                    bizes_id, bizes_nm, brch_nm,
                    inds_lcls_cd, inds_lcls_nm, inds_mcls_cd, inds_mcls_nm, inds_scls_cd, inds_scls_nm,
                    ctprvn_nm, signgu_nm, adong_nm, ldong_nm,
                    lno_adr, rdnm_adr, new_zipcd,
                    location, lon, lat
                ) VALUES (
                    :bizesId, :bizesNm, :brchNm,
                    :indsLclsCd, :indsLclsNm, :indsMclsCd, :indsMclsNm, :indsSclsCd, :indsSclsNm,
                    :ctprvnNm, :signguNm, :adongNm, :ldongNm,
                    :lnoAdr, :rdnmAdr, :newZipcd,
                    ST_GeomFromText('POINT(' || :lon || ' ' || :lat || ')', 4326),
                    :lon, :lat
                )
                ON CONFLICT (bizes_id) DO UPDATE SET
                    bizes_nm = EXCLUDED.bizes_nm,
                    location = EXCLUDED.location
            """)
            .beanMapped()
            .build();
    }
}