package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.AccountDetails;
import com.coreintegration.database.entity.AccountDetailsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {

    AccountDetails toDto(AccountDetailsEntity entity);

    AccountDetailsEntity toEntity(AccountDetails dto);

}
