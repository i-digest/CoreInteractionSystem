package com.coreintegration.database.mapper;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.database.entity.Loan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoansMapper {

    List<LoanDto> toDtoList(List<Loan> entity);

    List<Loan> toEntityList(List<LoanDto> dto);

    LoanDto toDto(Loan entity);

    Loan toEntity(LoanDto dto);

}
