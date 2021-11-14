package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BacklogRepositoryTest extends TestHelper {

    @Test
    void shouldSaveBacklog() {
        assertThat(backlogRepository.save(createValidSampleBacklog())).isNotNull();
    }
}