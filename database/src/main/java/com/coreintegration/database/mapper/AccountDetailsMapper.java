package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.AccountDetails;
import com.coreintegration.database.entity.AccountDetailsEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {

    List<AccountDetails> toDto(List<AccountDetailsEntity> entity);

    List<AccountDetailsEntity> toEntity(List<AccountDetails> dto);

    AccountDetails toDto(AccountDetailsEntity entity);

    AccountDetailsEntity toEntity(AccountDetails dto);

}
