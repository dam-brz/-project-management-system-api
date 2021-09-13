package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.ProjectIdException;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveOrUpdateProject(Project project) {
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
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
}
