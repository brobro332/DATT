package xyz.datt.domain.anchor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import xyz.datt.domain.anchor.dto.AnchorResponseDto;
import xyz.datt.domain.anchor.entity.AnchorSnapshot;
import xyz.datt.domain.place.dto.PlaceResponseDto;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class AnchorMapper {
    @Value("${domain.frontend}")
    private String frontendDomain;

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(target = "anchorId", source = "id")
    @Mapping(target = "shareUrl", source = "id", qualifiedByName = "idToShareUrl")
    @Mapping(target = "content", source = "contentJson", qualifiedByName = "jsonToContent")
    public abstract AnchorResponseDto toResponse(AnchorSnapshot entity);

    @Named("idToShareUrl")
    protected String idToShareUrl(String id) {
        return frontendDomain + "/share/" + id;
    }

    @Named("jsonToContent")
    protected Map<String, Map<String, List<PlaceResponseDto>>> jsonToContent(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 에러", e);
        }
    }
}
