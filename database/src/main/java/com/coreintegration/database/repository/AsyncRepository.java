package com.coreintegration.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@SuppressWarnings("UnusedReturnValue")
@NoRepositoryBean
public interface AsyncRepository<E, K> extends JpaRepository<E, K> {

    @Async
    @NonNull
    @Transactional
    default Future<List<E>> saveAllAsync(@NonNull Iterable<E> entities) {
        return CompletableFuture.completedFuture(saveAll(entities));
    }
}
