package com.coreintegration.database.repository;

import com.coreintegration.database.entity.Limit;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface LimitsRepository extends AsyncRepository<Limit, UUID> {

    @NonNull
    List<Limit> findAllByAccountIdIn(@NonNull Collection<UUID> ids);

}
