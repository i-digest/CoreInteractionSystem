package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LoansListResponse;
import com.coreintegration.commons.model.LoansResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoansService {

    @RateLimiter(name = "loansRateLimiter")
    public LoansResponse getById(String id) {
        return null;
    }

    @RateLimiter(name = "loansBulkRateLimiter")
    public LoansListResponse getByIds(List<String> ids) {
        return null;
    }
}
