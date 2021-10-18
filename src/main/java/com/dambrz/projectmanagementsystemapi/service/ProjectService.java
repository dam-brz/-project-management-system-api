package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.ProjectIdException;
import com.dambrz.projectmanagementsystemapi.exceptions.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project save(Project project, String username) {
        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID " + project.getProjectIdentifier().toUpperCase() + " already exists");
        }
    }

    public Project findProjectByProjectIdentifier(String projectIdentifier, String username) {

        Project project = projectRepository.findProjectByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID: " + projectIdentifier + " doesn't exists");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }



        return project;
    }

    public Iterable<Project> findAllProjects(String projectLeaderName) {
        return projectRepository.findAllByProjectLeader(projectLeaderName);
    }

    public void deleteProjectByIdentifier(String projectIdentifier, Principal principal) {
        projectRepository.delete(findProjectByProjectIdentifier(projectIdentifier, principal.getName()));
    }

    public Project updateProject(String projectIdentifier, Project project) {
        Project projectToUpdate = projectRepository.findProjectByProjectIdentifier(projectIdentifier);
        if (projectToUpdate == null) {
            throw new ProjectIdException("Project ID: " + projectIdentifier + " doesn't exists");
        }

        projectToUpdate.setProjectName(project.getProjectName());
        projectToUpdate.setUpdatedAt(project.getUpdatedAt());
        projectToUpdate.setDescription(project.getDescription());
        projectToUpdate.setEndDate(project.getEndDate());
        projectToUpdate.setStartDate(project.getStartDate());

        return projectRepository.save(projectToUpdate);
    }
}
