package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BacklogRepositoryTest extends TestHelper {

    @Test
    void shouldSaveBacklog() {
        Backlog backlog = createValidSampleBacklog();
        Backlog savedBacklog = backlogRepository.save(backlog);
        assertThat(savedBacklog)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(backlog);
    }
}