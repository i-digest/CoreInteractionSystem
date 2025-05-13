package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.database.mapper.DebitCardsMapper;
import com.coreintegration.database.repository.DebitCardsRepository;
import com.coreintegration.outbound.client.api.DebitCardsApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DebitCardsClient {

    private final DebitCardsApi api = new DebitCardsApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final DebitCardsRepository debitCardsRepository;
    private final DebitCardsMapper debitCardsMapper;

    @NonNull
    public List<DebitCardDto> getDebitCardsByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getDebitCarsByAccountIds",
                () -> api.getDebitCardsByAccountId(accountId).getDebitCards(),
                () -> debitCardsRepository.findAllById(Collections.singleton(accountId)).stream()
                        .map(debitCardsMapper::toDto)
                        .toList());
    }

    @NonNull
    public Collection<DebitCardDto> getDebitCardsByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getDebitCarsByAccountIds",
                        () -> api.getDebitCardsByAccountIds(accountIds).getDebitCards(),
                        () -> debitCardsRepository.findAllByAccountIdIn(accountIds).stream()
                                .map(debitCardsMapper::toDto)
                                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()))
                ).values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}
