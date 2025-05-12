package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.Balance;
import com.coreintegration.database.entity.BalanceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    List<Balance> toDto(List<BalanceEntity> entity);

    List<BalanceEntity> toEntity(List<Balance> dto);

    Balance toDto(BalanceEntity entity);

    BalanceEntity toEntity(Balance dto);

}
