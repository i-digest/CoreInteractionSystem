package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.fallbackengine.service.DebitCardsFallbackService;
import com.coreintegration.outbound.client.api.DebitCardsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DebitCardsClient {

    private final DebitCardsApi api = new DebitCardsApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final DebitCardsFallbackService fallbackService;

    @NonNull
    public List<DebitCardDto> getDebitCardsByAccountId(@NonNull final UUID accountId) {
        return circuitBreakerExecutor.run("getDebitCarsByAccountIds",
                () -> api.getDebitCardsByAccountId(accountId).getDebitCards(),
                () -> fallbackService.getFallbackByAccountId(accountId));
    }

    @NonNull
    public Collection<DebitCardDto> getDebitCardsByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getDebitCarsByAccountIds",
                        () -> api.getDebitCardsByAccountIds(accountIds).getDebitCards(),
                        () -> fallbackService.getFallbackByAccountIds(accountIds))
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}
