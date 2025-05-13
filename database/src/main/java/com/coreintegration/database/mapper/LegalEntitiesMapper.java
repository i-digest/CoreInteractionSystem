package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.entity.LegalEntity;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LegalEntitiesMapper {

    List<LegalEntityDto> toDtoList(Collection<LegalEntity> entity);

    List<LegalEntity> toEntityList(Collection<LegalEntityDto> dto);

    LegalEntityDto toDto(LegalEntity entity);

    LegalEntity toEntity(LegalEntityDto dto);

}
