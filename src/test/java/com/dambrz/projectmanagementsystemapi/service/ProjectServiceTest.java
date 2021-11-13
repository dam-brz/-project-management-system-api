package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.exceptions.ProjectIdException;
import com.dambrz.projectmanagementsystemapi.exceptions.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectServiceTest extends TestHelper {

    @Autowired
    private ProjectService projectService;

    @Test
    void save() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        assertThat(project).isNotNull();
    }

    @Test
    void saveShouldThrowProjectIdException() {
        User user = userRepository.save(createValidSampleUser());
        projectService.save(createValidSampleProject(), user.getUsername());
        Project project = createValidSampleProject();
        String username = user.getUsername();
        Assertions
                .assertThrows(ProjectIdException.class, () -> projectService.save(project, username));
    }

    @Test
    void findProjectByProjectIdentifier() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        assertThat(projectService
                        .findProjectByProjectIdentifier(
                                project.getProjectIdentifier(),
                                user.getUsername()))
                .isNotNull();
    }

    @Test
    void findProjectByProjectIdentifierShouldThrowProjectIdException() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        String username = user.getUsername();
        String projectIdentifier = project.getProjectIdentifier().concat("1");
        Assertions.assertThrows(ProjectIdException.class, () -> projectService.findProjectByProjectIdentifier(projectIdentifier, username));
    }

    @Test
    void findProjectByProjectIdentifierShouldThrowProjectNotFoundException() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        String username = user.getUsername().concat("1");
        String projectIdentifier = project.getProjectIdentifier();
        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.findProjectByProjectIdentifier(projectIdentifier, username));
    }

    @Test
    void findAllProjects() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        List<Project> projectList = (List<Project>) projectService.findAllProjects(project.getProjectLeader());
        assertThat(projectList).isNotNull();
    }

    @Test
    void deleteProjectByIdentifier() {
        User user = userRepository.save(createValidSampleUser());
        Project project = projectService.save(createValidSampleProject(), user.getUsername());
        projectService.deleteProjectByIdentifier(project.getProjectIdentifier(), user.getUsername());
        List<Project> projectList = (List<Project>) projectService.findAllProjects(project.getProjectLeader());
        assertThat(projectList.isEmpty()).isTrue();
    }

    @Test
    void updateProject() {
        User user = userRepository.save(createValidSampleUser());
        Project projectToUpdate = projectService.save(createValidSampleProject(), user.getUsername());
        projectToUpdate.setDescription("Updated");
        projectService.updateProject(projectToUpdate.getProjectIdentifier(),projectToUpdate, user.getUsername());
        Project updatedProject = projectService.findProjectByProjectIdentifier(projectToUpdate.getProjectIdentifier(), user.getUsername());
        assertThat(updatedProject.getDescription()).isEqualTo("Updated");
    }
}