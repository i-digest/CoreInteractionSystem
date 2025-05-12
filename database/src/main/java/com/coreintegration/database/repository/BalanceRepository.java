package com.coreintegration.database.repository;

import com.coreintegration.database.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, String> {

    List<BalanceEntity> findAllByAccountIdIn(Collection<String> ids);

    @Async
    @NonNull
    default void saveAllAsync(@NonNull Iterable<BalanceEntity> entities) {
        saveAll(entities);
    }
}
