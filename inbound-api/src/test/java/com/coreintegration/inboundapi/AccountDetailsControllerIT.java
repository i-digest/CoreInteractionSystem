package com.coreintegration.inboundapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountDetailsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnOkForDummyEndpoint() throws Exception {
        mockMvc.perform(get("/account-details/ping"))
               .andExpect(status().isOk());
    }
}