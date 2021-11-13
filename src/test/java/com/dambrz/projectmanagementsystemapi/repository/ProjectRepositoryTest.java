package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectRepositoryTest extends TestHelper {

    @Test
    void shouldSaveProject() {
        Project project = createValidSampleProject();
        Project savedProject = projectRepository.save(project);
        assertThat(savedProject)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(project);
    }

    @Test
    void shouldThrowsConstraintViolationExceptionCausedByNullProjectIdentifier() {
        Project project = createValidSampleProject();
        project.setProjectIdentifier(null);
        project.setProjectName(null);
        Assertions.assertThrows(ConstraintViolationException.class, () -> projectRepository.save(project));
    }

    @Test
    void shouldThrowsConstraintViolationExceptionCausedByNullProjectName() {
        Project project = createValidSampleProject();
        project.setProjectName(null);
        Assertions.assertThrows(ConstraintViolationException.class, () -> projectRepository.save(project));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TES", "testTestTest", ""})
    void shouldThrowsConstraintViolationException(String arg){
        Project project = createValidSampleProject();
        project.setProjectIdentifier(arg);
        Assertions.assertThrows(ConstraintViolationException.class, () -> projectRepository.save(project));
    }

    @Test
    void shouldGetProjectByProjectIdentifier() {
        List<Project> projectList = createSampleProjectsList();
        projectRepository.saveAll(projectList);
        Project existingProject = projectRepository.findProjectByProjectIdentifier(projectList.get(1).getProjectIdentifier());
        assertThat(existingProject).isNotNull();
    }

    @Test
    void shouldUpdateExistingProject() {
        List<Project> projectList = createSampleProjectsList();
        projectRepository.saveAll(projectList);
        Project projectToUpdate = projectRepository.findProjectByProjectIdentifier(projectList.get(1).getProjectIdentifier());
        projectToUpdate.setDescription("Upd Desc");
        projectRepository.save(projectToUpdate);
        assertThat(projectList.get(1)).isEqualTo(projectToUpdate);
    }

    @Test
    void shouldGetAllProjectLeaderProjects() {
        List<Project> sampleProjects = createSampleProjectsList();
        projectRepository.saveAll(sampleProjects);
        List<Project> userProjects = (List<Project>) projectRepository.findAllByProjectLeader(sampleProjects.get(0).getProjectLeader());
        assertThat(userProjects.isEmpty()).isFalse();
    }

}