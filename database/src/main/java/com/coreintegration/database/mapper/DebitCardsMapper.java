package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.database.entity.DebitCard;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DebitCardsMapper {

    List<DebitCardDto> toDtoList(Collection<DebitCard> entity);

    List<DebitCard> toEntityList(Collection<DebitCardDto> dto);

    DebitCardDto toDto(DebitCard entity);

    DebitCard toEntity(DebitCardDto dto);

}
