package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.BalanceDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BalanceClient {

    public BalanceDto getBalanceFromCore(UUID id) {
        return new BalanceDto();
    }

    public List<BalanceDto> getListOfBalancesFromCore(List<UUID> supplier) {
        return List.of(new BalanceDto());
    }
}
