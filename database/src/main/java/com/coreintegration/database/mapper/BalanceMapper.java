package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.database.entity.Balance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    List<BalanceDto> toDtoList(List<Balance> entity);

    List<Balance> toEntityList(List<BalanceDto> dto);

    BalanceDto toDto(Balance entity);

    Balance toEntity(BalanceDto dto);

}
