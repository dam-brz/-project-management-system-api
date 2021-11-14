package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.exceptions.UsernameAlreadyExistsError;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserServiceTest extends TestHelper {

    @Autowired
    private UserService userService;

    @BeforeEach
    void clearDb() {
        userRepository.deleteAll();
    }

    @Test
    void saveUser() {
        assertThat(userService.saveUser(createValidSampleUser())).isNotNull();
    }

    @Test
    void shouldThrow() {
        userRepository.save(createValidSampleUser());
        User userToSave = createValidSampleUser();
        Assertions.assertThrows(UsernameAlreadyExistsError.class, () -> userService.saveUser(userToSave));
    }
}