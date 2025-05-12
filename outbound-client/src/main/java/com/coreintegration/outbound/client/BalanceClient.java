package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.Balance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BalanceClient {

    public Balance getBalanceFromCore(String id) {
        return new Balance();
    }

    public List<Balance> getListOfBalancesFromCore(List<String> supplier) {
        return List.of(new Balance());
    }
}
