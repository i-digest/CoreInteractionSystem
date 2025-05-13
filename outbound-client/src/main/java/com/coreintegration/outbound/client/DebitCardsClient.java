package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.outbound.client.api.DebitCardsApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DebitCardsClient {

    private final DebitCardsApi api = new DebitCardsApi();

    @NonNull
    public List<DebitCardDto> getDebitCardsByAccountId(@NotBlank final UUID accountId) {
        return api.getDebitCardsByAccountId(accountId)
                .getDebitCards();
    }

    @NonNull
    public Collection<DebitCardDto> getDebitCardsByAccountIds(@NonNull final List<UUID> accountIds) {
        return api.getDebitCardsByAccountIds(accountIds)
                .getDebitCards()
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
