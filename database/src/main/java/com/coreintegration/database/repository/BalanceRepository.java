package com.coreintegration.database.repository;

import com.coreintegration.database.entity.Balance;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface BalanceRepository extends AsyncRepository<Balance, UUID> {

    @NonNull
    List<Balance> findAllByAccountIdIn(@NonNull Collection<UUID> ids);

}
