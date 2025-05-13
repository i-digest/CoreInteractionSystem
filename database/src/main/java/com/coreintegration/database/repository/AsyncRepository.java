package com.coreintegration.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AsyncRepository<E, K> extends JpaRepository<E, K> {

    @Async
    @NonNull
    @Transactional
    default void saveAllAsync(@NonNull Iterable<E> entities) {
        saveAll(entities);
    }
}
