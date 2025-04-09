package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.BalanceListResponse;
import com.coreintegration.commons.model.BalanceResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalancesService {


    @RateLimiter(name = "balancesRateLimiter")
    public BalanceResponse getById(String id) {
        return null;
    }

    @RateLimiter(name = "balancesBulkRateLimiter")
    public BalanceListResponse getByIds(List<String> ids) {
        return null;
    }
}
