package com.coreintegration.database.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncDatabaseUpdateService {

    @Async
    @Transactional
    public void update() {

    }

}
