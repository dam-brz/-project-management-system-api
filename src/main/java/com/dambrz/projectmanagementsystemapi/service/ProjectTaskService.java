package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectService projectService;

    public ProjectTaskService(ProjectTaskRepository projectTaskRepository, ProjectService projectService) {
        this.projectTaskRepository = projectTaskRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        try {
            Backlog backlog = projectService.findProjectByProjectIdentifier(projectIdentifier, username).getBacklog();
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
            throw new ProjectNotFoundException("Project with ID: " + projectIdentifier + " Not Found.");
        }
    }

    public Set<ProjectTask> findBacklogByProjectIdentifier(String projectIdentifier, String username) {

        projectService.findProjectByProjectIdentifier(projectIdentifier, username);
        return projectTaskRepository.findProjectTaskByProjectIdentifierOrderByPriority(projectIdentifier);
    }

    public ProjectTask findProjectTaskByProjectTaskSequence(String projectIdentifier, String projectTaskSequence, String username) {

        //make sure that searching for existing backlog
        projectService.findProjectByProjectIdentifier(projectIdentifier, username);

        //make sure that task exist
        ProjectTask task = projectTaskRepository.findProjectTaskByProjectSequence(projectTaskSequence);
        if (task == null) {
            throw new ProjectNotFoundException("Project Task '" + projectTaskSequence + "' not found.");
        }

        //make sure that project task correspondents to right project
        if (!task.getProjectIdentifier().equals(projectIdentifier)) {
            throw new ProjectNotFoundException("Project Task '" + projectTaskSequence + "' doesn't exists in project : " + projectIdentifier);
        }

        return task;
    }

    public ProjectTask updateProjectTask(ProjectTask updatedProjectTask, String projectIdentifier, String projectTaskSequence, String username) {

        ProjectTask task = findProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, username);

        task.setAcceptanceCriteria(updatedProjectTask.getAcceptanceCriteria());
        task.setPriority(updatedProjectTask.getPriority());
        task.setStatus(updatedProjectTask.getStatus());
        task.setSummary(updatedProjectTask.getSummary());
        return projectTaskRepository.save(task);
    }

    public void deleteProjectTaskByProjectTaskSequence(String projectIdentifier, String projectTaskSequence, String username) {
        projectTaskRepository.delete(findProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, username));
    }
}
