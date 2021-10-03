package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.repository.BacklogRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProjectTaskService {

    private final BacklogRepository backlogRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectRepository projectRepository;

    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try {
            Backlog backlog = backlogRepository.findBacklogByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);

            int backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            if (projectTask.getStatus() == null || projectTask.getStatus().isBlank()) {
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project Not Found.");
        }
    }

    public Set<ProjectTask> findBacklogByProjectIdentifier(String projectIdentifier) {

        Project project = projectRepository.findProjectByProjectIdentifier(projectIdentifier);

        if (project == null) {
            throw new ProjectNotFoundException("Project Not Found.");
        }

        return projectTaskRepository.findProjectTaskByProjectIdentifierOrderByPriority(projectIdentifier);
    }
}
