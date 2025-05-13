package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.outbound.client.api.LimitsApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LimitsClient {

    private final LimitsApi api = new LimitsApi();

    @NonNull
    public List<LimitDto> getLimitsByAccountId(@NotBlank final UUID accountId) {
        return api.getLimitsByAccountId(accountId)
                .getLimits();
    }

    @NonNull
    public Collection<LimitDto> getLimitsByAccountIds(@NonNull final List<UUID> accountIds) {
        return api.getLimitsByAccountIds(accountIds)
                .getLimits()
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
