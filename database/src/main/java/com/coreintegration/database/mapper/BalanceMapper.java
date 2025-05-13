package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.database.entity.Balance;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    List<BalanceDto> toDtoList(Collection<Balance> entity);

    List<Balance> toEntityList(Collection<BalanceDto> dto);

    BalanceDto toDto(Balance entity);

    Balance toEntity(BalanceDto dto);

}
