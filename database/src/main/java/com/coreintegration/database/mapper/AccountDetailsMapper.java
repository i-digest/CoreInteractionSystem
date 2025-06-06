package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.database.entity.AccountDetails;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                BalanceMapper.class,
                DebitCardsMapper.class,
                LegalEntitiesMapper.class,
                LimitsMapper.class,
                LoansMapper.class
        })
public interface AccountDetailsMapper {

    List<AccountDetailsDto> toDtoList(Collection<AccountDetails> entity);

    List<AccountDetails> toEntityList(Collection<AccountDetailsDto> dto);

    AccountDetailsDto toDto(AccountDetails entity);

    AccountDetails toEntity(AccountDetailsDto dto);

}
