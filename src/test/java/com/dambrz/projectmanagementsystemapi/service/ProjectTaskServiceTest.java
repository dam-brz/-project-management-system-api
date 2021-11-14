package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.exceptions.ProjectIdException;
import com.dambrz.projectmanagementsystemapi.exceptions.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ProjectTaskServiceTest extends TestHelper {

    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private ProjectService projectService;

    @BeforeEach
    void clearDb() {
        userRepository.deleteAll();
        projectRepository.deleteAll();
        backlogRepository.deleteAll();
    }

    @Test
    void addProjectTask() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        ProjectTask projectTask = createValidSampleProjectTask();
        ProjectTask validProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        assertThat(validProjectTask).isNotNull();
    }

    @Test
    void shouldAutoSetProjectTaskPriority() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setPriority(null);
        projectTask.setBacklog(backlog);
        ProjectTask validProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        assertThat(validProjectTask.getPriority()).isEqualTo(3);
    }

    @Test
    void shouldAutoSetProjectTaskPriorityTo1() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setPriority(0);
        projectTask.setBacklog(backlog);
        ProjectTask validProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        assertThat(validProjectTask.getPriority()).isEqualTo(3);
    }

    @Test
    void shouldAutoSetProjectTaskStatusToTodo() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setStatus(null);
        projectTask.setBacklog(backlog);
        ProjectTask validProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        assertThat(validProjectTask.getStatus()).isEqualTo("TO_DO");
    }

    @Test
    void shouldAutoSetProjectTaskStatusToTodoT() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setStatus("");
        projectTask.setBacklog(backlog);
        ProjectTask validProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        assertThat(validProjectTask.getStatus()).isEqualTo("TO_DO");
    }

    @Test
    void findBacklogByProjectIdentifier() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setBacklog(backlog);
        projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        Set<ProjectTask> projectTasks = projectTaskService.findBacklogByProjectIdentifier(project.getProjectIdentifier(), user.getUsername());
        assertThat(projectTasks.isEmpty()).isFalse();
    }

    @Test
    void findProjectTaskByProjectTaskSequence() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setBacklog(backlog);
        ProjectTask savedProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        ProjectTask taskToGet = projectTaskService.findProjectTaskByProjectTaskSequence(project.getProjectIdentifier(), savedProjectTask.getProjectSequence(), user.getUsername());
        assertThat(taskToGet).isNotNull();
    }

    @Test
    void findProjectTaskByProjectTaskSequenceShouldThrowProjectNotFoundException() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setBacklog(backlog);
        projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        String projectIdentifier = project.getProjectIdentifier();
        String username = user.getUsername();
        Assertions
                .assertThrows(ProjectNotFoundException.class, () -> projectTaskService.findProjectTaskByProjectTaskSequence(projectIdentifier, null, username));
    }

    @Test
    void findProjectTaskByProjectTaskSequenceShouldThrowProjectNotFoundException2() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setBacklog(backlog);
        ProjectTask savedProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        String projectIdentifier = project.getProjectIdentifier().concat("1");
        String projectSequence = savedProjectTask.getProjectSequence();
        String username = user.getUsername();
        Assertions
                .assertThrows(ProjectIdException.class, () -> projectTaskService.findProjectTaskByProjectTaskSequence(projectIdentifier, projectSequence, username));
    }

    @Test
    void updateProjectTask() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setBacklog(backlog);
        ProjectTask taskToUpdate = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        taskToUpdate.setStatus("DONE");
        projectTaskService.updateProjectTask(taskToUpdate, project.getProjectIdentifier(), taskToUpdate.getProjectSequence(), user.getUsername());
        ProjectTask updatedProjectTask = projectTaskService.findProjectTaskByProjectTaskSequence(project.getProjectIdentifier(), taskToUpdate.getProjectSequence(), user.getUsername());
        assertThat(updatedProjectTask)
                .isNotEqualTo(taskToUpdate);
    }

    @Test
    void deleteProjectTaskByProjectTaskSequence() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        Backlog backlog = backlogRepository.save(createValidSampleBacklog());
        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setBacklog(backlog);
        ProjectTask savedProjectTask = projectTaskService.addProjectTask(project.getProjectIdentifier(), projectTask, user.getUsername());
        projectTaskService.deleteProjectTaskByProjectTaskSequence(project.getProjectIdentifier(), savedProjectTask.getProjectSequence(), user.getUsername());
        Set<ProjectTask> projectTasks = projectTaskService.findBacklogByProjectIdentifier(project.getProjectIdentifier(), user.getUsername());
        assertThat(projectTasks.isEmpty()).isTrue();
    }
}