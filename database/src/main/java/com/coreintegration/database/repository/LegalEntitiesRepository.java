package com.coreintegration.database.repository;

import com.coreintegration.database.entity.LegalEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface LegalEntitiesRepository extends AsyncRepository<LegalEntity, UUID> {

    @NonNull
    List<LegalEntity> findAllByAccountIdIn(@NonNull Collection<UUID> ids);

}