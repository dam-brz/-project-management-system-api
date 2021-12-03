package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.ProjectIdException;
import com.dambrz.projectmanagementsystemapi.exceptions.exception.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.mapper.ProjectMapper;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectRequest;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectDto;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static com.dambrz.projectmanagementsystemapi.exceptions.ExceptionMessageContent.*;

@Service
public class ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectMapper projectMapper, ProjectRepository projectRepository, UserService userService) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public void save(CreateProjectRequest createProjectRequest, String username) {
        if (isProjectIdentifierExists(createProjectRequest.getProjectIdentifier()))
            throw new ProjectIdException(PROJECT_IDENTIFIER_ALREADY_EXISTS_MSG);
        else projectRepository.save(createProject(createProjectRequest, username));
    }

    public ProjectDto findProjectByProjectIdentifier(String projectIdentifier, String username) {
        ProjectDto project = projectMapper.getProjectDto(findProjectByProjectIdentifier(projectIdentifier));
        if (!project.getProjectLeader().equals(username))
            throw new ProjectNotFoundException(PROJECT_NOT_FOUND_IN_YOUR_ACCOUNT_MSG);
        return project;
    }

    public Project findProjectByProjectIdentifier(String projectIdentifier) {
        return projectRepository
                .findByProjectIdentifier(projectIdentifier.toUpperCase())
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_DOESNT_EXISTS_MSG));
    }

    public List<ProjectDto> findAllProjectsDto(Principal principal) {
        return projectMapper
                .getAllUserProjectsDto(
                        getAllUserProjects(userService.findUserByUsername(principal.getName())));
    }

    public boolean delete(String projectIdentifier, String username) {
        Project project = findProjectByProjectIdentifier(projectIdentifier);
        boolean success = false;

        if (project.getProjectLeader().getUsername().equals(username)) {
            success = true;
            projectRepository.delete(project);
        }

        return success;
    }

    public void update(String projectIdentifier, ProjectDto updatedProject, String username) {
        Project projectToUpdate = findProjectByProjectIdentifier(projectIdentifier);

        if (!projectToUpdate.getProjectLeader().getUsername().equals(username))
            throw new ProjectNotFoundException(PROJECT_NOT_FOUND_IN_YOUR_ACCOUNT_MSG);

        projectToUpdate.setProjectName(updatedProject.getProjectName());
        projectToUpdate.setDescription(updatedProject.getDescription());
        projectToUpdate.setEndDate(updatedProject.getEndDate());
        projectToUpdate.setStartDate(updatedProject.getStartDate());
        projectRepository.save(projectToUpdate);
    }

    private Project createProject(CreateProjectRequest createProjectRequest, String username) {
        Backlog backlog = new Backlog();
        Project project = projectMapper.getProjectFromCreateProjectRequest(createProjectRequest);
        project.setProjectLeader(userService.findUserByUsername(username));
        project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
        project.setBacklog(backlog);
        project.setStartDate(createProjectRequest.getStartDate());
        project.setEndDate(createProjectRequest.getEndDate());
        backlog.setProject(project);
        backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
        return project;
    }

    private boolean isProjectIdentifierExists(String projectIdentifier) {
        return projectRepository.existsByProjectIdentifier(projectIdentifier.toUpperCase());
    }

    private List<Project> getAllUserProjects(User user) {
        return projectRepository.findAllByProjectLeader(user);
    }
}
