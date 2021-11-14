package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProjectRepositoryTest extends TestHelper {

    @Test
    void shouldSaveProject() {
        assertThat(projectRepository.save(createValidSampleProject())).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("createSampleInvalidProjectsList")
    void shouldThrowsConstraintViolationException(Project arg){
        Assertions.assertThrows(ConstraintViolationException.class, () -> projectRepository.save(arg));
    }

    @Test
    void shouldGetProjectByProjectIdentifier() {
        List<Project> projectList = getSampleProjectsList(projectRepository.saveAll(createSampleProjectsList()));
        assertThat(projectRepository.findProjectByProjectIdentifier(projectList.get(1).getProjectIdentifier())).isNotNull();
    }

    @Test
    void shouldUpdateExistingProject() {
        List<Project> projectList = getSampleProjectsList(projectRepository.saveAll(createSampleProjectsList()));
        Project projectToUpdate = projectRepository.findProjectByProjectIdentifier(projectList.get(1).getProjectIdentifier());
        projectToUpdate.setDescription(projectList.get(2).getDescription());
        projectRepository.save(projectToUpdate);
        Project updatedProject = projectRepository.findProjectByProjectIdentifier(projectList.get(1).getProjectIdentifier());
        assertThat(projectToUpdate.getDescription()).isEqualTo(updatedProject.getDescription());
    }

    @Test
    void shouldGetAllProjectLeaderProjects() {
        List<Project> projectList = getSampleProjectsList(projectRepository.saveAll(createSampleProjectsList()));
        List<Project> userProjects = getSampleProjectsList(projectRepository.findAllByProjectLeader(projectList.get(0).getProjectLeader()));
        assertThat(userProjects.isEmpty()).isFalse();
    }

}