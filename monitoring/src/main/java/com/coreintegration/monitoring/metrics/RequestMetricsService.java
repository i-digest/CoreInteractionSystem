package com.coreintegration.monitoring.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class RequestMetricsService {

    private final MeterRegistry registry;

    public RequestMetricsService(MeterRegistry registry) {
        this.registry = registry;
    }

    public void countRequest(String type) {
        registry.counter("coreintegration.request", "type", type).increment();
    }
}

