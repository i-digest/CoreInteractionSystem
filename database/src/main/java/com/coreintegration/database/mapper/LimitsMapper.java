package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.database.entity.Limit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LimitsMapper {

    List<LimitDto> toDtoList(List<Limit> entity);

    List<Limit> toEntityList(List<LimitDto> dto);

    LimitDto toDto(Limit entity);

    Limit toEntity(LimitDto dto);

}
