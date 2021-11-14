package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest extends TestHelper {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void testRegister() throws Exception {
        performRegister(getValidUserAsJsonString()).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("getInvalidUsersAsJson")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testRegisterShouldReturnBadRequest(String arg) throws Exception {
        userRepository.save(createValidSampleUser());
        performRegister(arg).andExpect(status().isBadRequest());
    }

    @Test
    void testAuthenticateUser() throws Exception {
        performRegister(getValidUserAsJsonString());
        performLogin(getValidLoginRequestAsJsonString()).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("getInvalidLoginRequestsAsJson")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testAuthenticateUserShouldGetBadRequest(String arg) throws Exception {
        performRegister(getValidUserAsJsonString());
        performLogin(arg).andExpect(status().is4xxClientError());
    }



    protected ResultActions performRegister(String userAsJsonString) throws Exception {
        return mockMvc.perform(
                post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJsonString));
    }

    protected ResultActions performLogin(String userAsJsonString) throws Exception {
        return mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJsonString));
    }

}