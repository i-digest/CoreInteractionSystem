package com.coreintegration.database.service;

import com.coreintegration.database.repository.AsyncRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AsyncDatabaseUpdateService<T> {

    private final AsyncRepository<T, UUID> asyncRepository;

    @Async
    @SneakyThrows
    @Transactional
    public List<T> update(final List<T> entity) {
        return asyncRepository.saveAllAsync(entity).get();
    }

}
