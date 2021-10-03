package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.ProjectIdException;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project save(Project project) {
        try{
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

    public Project findProjectByProjectIdentifier(String projectIdentifier) {
        Project project = projectRepository.findProjectByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID: " + projectIdentifier + " doesn't exists");
        }

        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findProjectByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID: " + projectIdentifier + " doesn't exists");
        }
        projectRepository.delete(project);
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
