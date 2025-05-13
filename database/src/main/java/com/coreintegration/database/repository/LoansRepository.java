package com.coreintegration.database.repository;

import com.coreintegration.database.entity.Loan;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoansRepository extends AsyncRepository<Loan, UUID> {

    @NonNull
    List<Loan> findAllByAccountIdIn(@NonNull Collection<UUID> ids);

}