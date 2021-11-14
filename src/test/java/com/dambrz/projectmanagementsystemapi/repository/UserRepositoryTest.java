package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest extends TestHelper {

    @Test
    void shouldSaveUser() {
        assertThat(userRepository.save(createValidSampleUser())).isNotNull();
    }

    @Test
    void shouldThrowsConstraintViolationException() {
        User user = createInValidSampleUser();
        Assertions.assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void shouldGetExistingUserByUsername() {
        List<User> users = createSampleUsersList();
        userRepository.saveAll(users);
        assertThat(userRepository.findByUsername(users.get(0).getUsername()))
                .usingRecursiveComparison()
                .ignoringFields("id", "createAt", "confirmPassword")
                .isEqualTo(users.get(0));
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
        assertThat(userRepository.getById(savedUsers.get(1).getId()))
                .usingRecursiveComparison()
                .ignoringFields("id", "projects", "createAt", "confirmPassword")
                .isEqualTo(users.get(1));
    }

    @Test
    void shouldThrowsJpaObjectRetrievalFailureException() {
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> userRepository.getById(6L));
    }
}