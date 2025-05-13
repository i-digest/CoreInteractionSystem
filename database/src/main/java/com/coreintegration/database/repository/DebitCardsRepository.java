package com.coreintegration.database.repository;

import com.coreintegration.database.entity.DebitCard;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface DebitCardsRepository extends AsyncRepository<DebitCard, UUID> {

    @NonNull
    List<DebitCard> findAllByAccountIdIn(@NonNull Collection<UUID> ids);

}
