package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectTaskRepositoryTest extends TestHelper {
//
    @Test
    void shouldSaveProjectTask() {
        ProjectTask projectTask = createInvalidSampleProjectTask();
        Backlog backlog = createValidSampleBacklog();
        backlogRepository.save(backlog);
        projectTask.setBacklog(backlog);
        ProjectTask savedProjectTask = projectTaskRepository.save(projectTask);
        assertThat(savedProjectTask)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(projectTask);
    }

    @Test
    void shouldThrowsDataIntegrityViolationExceptionCausedByNullBacklog() {
        ProjectTask projectTask = createInvalidSampleProjectTask();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> projectTaskRepository.save(projectTask));
    }

    @Test
    void shouldGetProjectTasks() {
        ProjectTask projectTask = createInvalidSampleProjectTask();
        ProjectTask projectTask2 = createInvalidSampleProjectTask();
        projectTask2.setProjectSequence("TEST1-2");
        Backlog backlog = createValidSampleBacklog();
        backlogRepository.save(backlog);
        projectTask.setBacklog(backlog);
        projectTask2.setBacklog(backlog);
        List<ProjectTask> projectTasks = List.of(projectTask, projectTask2);
        projectTaskRepository.saveAll(projectTasks);
        Set<ProjectTask> savedProjectTasks = projectTaskRepository
                .findProjectTaskByProjectIdentifierOrderByPriority(projectTask.getProjectIdentifier());
        assertThat(savedProjectTasks.isEmpty()).isFalse();
    }

    @Test
    void shouldNotGetAnyProjectTasks() {
        ProjectTask projectTask = createInvalidSampleProjectTask();
        ProjectTask projectTask2 = createInvalidSampleProjectTask();
        projectTask2.setProjectSequence("TEST1-2");
        Backlog backlog = createValidSampleBacklog();
        backlogRepository.save(backlog);
        projectTask.setBacklog(backlog);
        projectTask2.setBacklog(backlog);
        List<ProjectTask> projectTasks = List.of(projectTask, projectTask2);
        projectTaskRepository.saveAll(projectTasks);
        Set<ProjectTask> savedProjectTasks = projectTaskRepository
                .findProjectTaskByProjectIdentifierOrderByPriority(projectTask.getProjectIdentifier().concat("123"));
        assertThat(savedProjectTasks.isEmpty()).isTrue();
    }
}