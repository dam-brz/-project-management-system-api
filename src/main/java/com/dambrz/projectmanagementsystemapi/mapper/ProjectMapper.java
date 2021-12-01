package com.dambrz.projectmanagementsystemapi.mapper;

import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectDto;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public Project getProjectFromCreateProjectRequest(CreateProjectRequest createProjectRequest) {
        return new Project(
                createProjectRequest.getProjectName(),
                createProjectRequest.getProjectIdentifier(),
                createProjectRequest.getDescription());
    }

    public ProjectDto getProjectDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getProjectName(),
                project.getProjectIdentifier(),
                project.getDescription(),
                project.getProjectLeader().getUsername(),
                project.getStartDate(),
                project.getEndDate(),
                project.getCreateAt(),
                project.getUpdatedAt());
    }

    public List<ProjectDto> getAllUserProjectsDto(List<Project> projects) {
        return projects.stream()
                .map(this::getProjectDto)
                .collect(Collectors.toList());
    }
}
