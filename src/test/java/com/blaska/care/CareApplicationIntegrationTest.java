package com.blaska.care;

import com.blaska.care.application.*;
import com.blaska.utils.care.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CareApplicationIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void fullScenario() throws Exception {
        // given
        var userToken = "usertoken1234";
        var csrAuthorization = "csrAuthorization";

        // when
        // Step 1
        var message = new MessageRequest("Hello, I have an issue with my new phone");
        var result = mvc.perform(MockMvcRequestBuilders.post("/messages")
                        .content(TestUtils.toJson(message))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, userToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        var messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);

        // Step 2
        var clientCase = new ClientCaseRequest(messageResponse.getMessageId());
        var clientCaseURI = mvc.perform(MockMvcRequestBuilders.post("/cases")
                        .content(TestUtils.toJson(clientCase))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, csrAuthorization)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.LOCATION);

        // Step 3
        var supportMessage = new MessageRequest("I am Sonia, and I will do my best to help you. " +
                "What is your phone brand and model?");
        mvc.perform(MockMvcRequestBuilders.post(clientCaseURI + "/messages")
                        .content(TestUtils.toJson(supportMessage))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, csrAuthorization)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Step 4
        var patchCustomerCase = new ClientCasePatch("KA-18B6");
        mvc.perform(MockMvcRequestBuilders.patch(clientCaseURI)
                        .content(TestUtils.toJson(patchCustomerCase))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, csrAuthorization)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Step 5
        var getResponse = mvc.perform(MockMvcRequestBuilders.get("/cases/KA-18B6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, csrAuthorization)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        var expectedMessages = List.of(
                new MessageDTO("Jérémie Durand", "Hello, I have an issue with my new phone"),
                new MessageDTO("Sonia Valentin", "I am Sonia, and I will do my best to help you. " +
                        "What is your phone brand and model?")
        );
        var expectedResult = new ClientCaseDTO(1, "Jérémie Durand", "KA-18B6", expectedMessages);
        var allClientCases = new ObjectMapper().readValue(getResponse.getResponse().getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<List<ClientCaseDTO>>(){});
        assertThat(allClientCases.size()).isEqualTo(1);
        assertThat(allClientCases)
                .containsExactly(expectedResult);
    }
}
