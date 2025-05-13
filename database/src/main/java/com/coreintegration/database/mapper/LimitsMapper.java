package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.database.entity.Limit;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LimitsMapper {

    List<LimitDto> toDtoList(Collection<Limit> entity);

    List<Limit> toEntityList(Collection<LimitDto> dto);

    LimitDto toDto(Limit entity);

    Limit toEntity(LimitDto dto);

}
