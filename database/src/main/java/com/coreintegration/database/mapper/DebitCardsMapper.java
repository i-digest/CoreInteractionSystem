package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.database.entity.DebitCard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DebitCardsMapper {

    List<DebitCardDto> toDtoList(List<DebitCard> entity);

    List<DebitCard> toEntityList(List<DebitCardDto> dto);

    DebitCardDto toDto(DebitCard entity);

    DebitCard toEntity(DebitCardDto dto);

}
