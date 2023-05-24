package com.blaska.care;

import com.blaska.care.application.MessageRequest;
import com.blaska.care.application.MessageResourceV2;
import com.blaska.utils.care.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResourceV2.class)
class MessageResourceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturn202WhenMessageCreatedByPOST() throws Exception {
        var message = new MessageRequest("Hello, I have an issue with my new phone");
        var authorization = "usertoken1234";
        mvc.perform(MockMvcRequestBuilders.post("/messages")
                        .content(TestUtils.toJson(message))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, authorization)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowExceptionWhenMessageNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/messages/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUserMessagesWhenValidCustomerPresentInToken() throws Exception {
        var authorization = "usertoken1234";
        var result = mvc.perform(MockMvcRequestBuilders.get("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, authorization))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result).isNotNull();
    }
}