package com.blaska.care;

import com.blaska.utils.care.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResource.class)
class MessageResourceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturn202WhenMessageCreatedByPOST() throws Exception {
        var message = new MessageRequest("Jérémie Durand", "Hello, I have an issue with my new phone");
        mvc.perform(MockMvcRequestBuilders.post("/message")
                .content(TestUtils.toJson(message))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowExceptionWhenMessageNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/message/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}