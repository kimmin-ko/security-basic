package com.sp.sec.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserAccessTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("1. user 로 user 페이지를 접근할 수 있다.")
    @WithMockUser(username = "user", roles = {"USER"})
    void test_user_access_userpage() throws Exception {
        String response = mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SecurityMessage message = objectMapper.readValue(response, SecurityMessage.class);

        assertThat(message.getMessage()).isEqualTo("user page");
    }

    @Test
    @DisplayName("2. user 로 admin 페이지를 접근할 수 없다.")
    @WithMockUser(username = "user", roles = {"USER"})
    void test_user_cannot_access_adminpage() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().is4xxClientError());
    }
    
}
