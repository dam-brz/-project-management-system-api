package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.exceptions.UsernameAlreadyExistsError;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserServiceTest extends TestHelper {

    @Autowired
    private UserService userService;

    @Test
    void saveUser() {
        User user = createValidSampleUser();
        User savedUser = userService.saveUser(user);
        assertThat(savedUser).isNotNull();
    }

    @Test
    void shouldThrow() {
        User user =createValidSampleUser();
        userService.saveUser(createValidSampleUser());
        Assertions.assertThrows(UsernameAlreadyExistsError.class, () -> userService.saveUser(user));
    }
}