package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.entity.LegalEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LegalEntitiesMapper {

    List<LegalEntityDto> toDtoList(List<LegalEntity> entity);

    List<LegalEntity> toEntityList(List<LegalEntityDto> dto);

    LegalEntityDto toDto(LegalEntity entity);

    LegalEntity toEntity(LegalEntityDto dto);

}
