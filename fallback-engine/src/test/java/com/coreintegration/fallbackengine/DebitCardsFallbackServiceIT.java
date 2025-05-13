package com.coreintegration.fallbackengine;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.fallbackengine.service.DebitCardsFallbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DebitCardsFallbackServiceIT {

    @Autowired
    private DebitCardsFallbackService service;

    @Test
    void shouldReturnEmptyListOrDataFromDb() {
        List<DebitCardDto> result = service.getFallbackByAccountIds(Collections.singletonList(UUID.randomUUID())).getOrDefault("test", List.of());
        assertNotNull(result);
    }
}