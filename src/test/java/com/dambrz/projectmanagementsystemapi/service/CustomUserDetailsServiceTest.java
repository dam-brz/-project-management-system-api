package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomUserDetailsServiceTest extends TestHelper {

    @Autowired
    protected CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername() {
        User user = userRepository.save(createValidSampleUser());
        User savedUser = (User) userDetailsService.loadUserByUsername(user.getUsername());
        assertThat(savedUser).isNotNull();
    }

    @Test
    void shouldThrowUsernameNotFoundException() {
        User user = userRepository.save(createValidSampleUser());
        String username = user.getUsername().concat("1");
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void loadUserById() {
        User user = userRepository.save(createValidSampleUser());
        User savedUser = userDetailsService.loadUserById(user.getId());
        assertThat(savedUser).isNotNull();
    }

    @Test
    void shouldThrowJpaObjectRetrievalFailureException() {
        User user = userRepository.save(createValidSampleUser());
        Long userId = user.getId()+1L;
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> userDetailsService.loadUserById(userId));
    }
}