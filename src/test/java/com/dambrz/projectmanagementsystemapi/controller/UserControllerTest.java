package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends TestHelper {

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
}