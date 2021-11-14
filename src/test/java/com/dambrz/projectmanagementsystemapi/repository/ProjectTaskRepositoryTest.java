package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProjectTaskRepositoryTest extends TestHelper {

    @Test
    void shouldSaveProjectTask() {
        assertThat(projectTaskRepository.save(createValidSampleProjectTask())).isNotNull();
    }

    @Test
    void shouldThrowsDataIntegrityViolationExceptionCausedByNullBacklog() {
        ProjectTask projectTask = createInvalidSampleProjectTask();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> projectTaskRepository.save(projectTask));
    }

    @Test
    void shouldGetProjectTasks() {
        List<ProjectTask> projectTasks =  (List<ProjectTask>) projectTaskRepository.saveAll(createProjectTaskList());
        Set<ProjectTask> savedProjectTasks = projectTaskRepository
                .findProjectTaskByProjectIdentifierOrderByPriority(projectTasks.get(1).getProjectIdentifier());
        assertThat(savedProjectTasks.isEmpty()).isFalse();
    }

    @Test
    void shouldNotGetAnyProjectTasks() {
        List<ProjectTask> projectTasks =  (List<ProjectTask>) projectTaskRepository.saveAll(createProjectTaskList());
        Set<ProjectTask> savedProjectTasks = projectTaskRepository
                .findProjectTaskByProjectIdentifierOrderByPriority(projectTasks.get(1).getProjectIdentifier().concat("123"));
        assertThat(savedProjectTasks.isEmpty()).isTrue();
    }
}