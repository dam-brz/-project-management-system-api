package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
//import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest extends TestHelper {

    @Test
    void shouldSaveUser() {
        User user = createValidSampleUser();
        User savedUser = userRepository.save(user);
        assertThat(savedUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void shouldThrowsConstraintViolationException() {
        User user = createValidSampleUser();
        user.setUsername(null);
        Assertions.assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void shouldGetExistingUserByUsername() {
        List<User> users = createSampleUsersList();
        userRepository.saveAll(users);
        User existingUser = userRepository.findByUsername(users.get(1).getUsername());
        assertThat(existingUser).isEqualTo(users.get(1));
    }

    @Test
    void shouldNotGetExistingUserByUsername() {
        List<User> users = createSampleUsersList();
        userRepository.saveAll(users);
        User existingUser = userRepository.findByUsername("stachu6@jones.pl");
        assertThat(existingUser).isNull();
    }

    @Test
    void shouldGetExistingUserById() {
        List<User> users = createSampleUsersList();
        List<User> savedUsers = (List<User>) userRepository.saveAll(users);
        User existingUser = userRepository.getById(savedUsers.get(1).getId());
        assertThat(existingUser).isEqualTo(users.get(1));
    }

    @Test
    void shouldThrowsJpaObjectRetrievalFailureException() {
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> userRepository.getById(6L));
    }
}