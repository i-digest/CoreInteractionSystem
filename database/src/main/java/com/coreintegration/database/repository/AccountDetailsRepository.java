package com.coreintegration.database.repository;

import com.coreintegration.database.entity.AccountDetails;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccountDetailsRepository extends AsyncRepository<AccountDetails, UUID> {

    @NonNull
    List<AccountDetails> findAllByIdIn(@NonNull Collection<UUID> ids);

}
