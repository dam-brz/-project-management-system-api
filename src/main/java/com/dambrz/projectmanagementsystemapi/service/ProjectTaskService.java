package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.DateException;
import com.dambrz.projectmanagementsystemapi.exceptions.exception.ProjectNotFoundException;
import com.dambrz.projectmanagementsystemapi.exceptions.exception.TaskNotFoundException;
import com.dambrz.projectmanagementsystemapi.mapper.ProjectTaskMapper;
import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.model.enums.EPriority;
import com.dambrz.projectmanagementsystemapi.model.enums.EStatus;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectTaskDto;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectTaskRequest;
import com.dambrz.projectmanagementsystemapi.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static com.dambrz.projectmanagementsystemapi.exceptions.ExceptionMessageContent.*;

@Service
public class ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectService projectService;
    private final ProjectTaskMapper projectTaskMapper;

    public ProjectTaskService(ProjectTaskRepository projectTaskRepository, ProjectService projectService, ProjectTaskMapper projectTaskMapper) {
        this.projectTaskRepository = projectTaskRepository;
        this.projectService = projectService;
        this.projectTaskMapper = projectTaskMapper;
    }

    public void save(String projectIdentifier, CreateProjectTaskRequest createProjectTaskRequest, String username) {
            Project project = projectService.findProjectByProjectIdentifier(projectIdentifier);

            if (!project.getProjectLeader().getUsername().equals(username))
                throw new ProjectNotFoundException(PROJECT_NOT_FOUND_IN_YOUR_ACCOUNT_MSG);

            ProjectTask projectTask = projectTaskMapper.getProjectTask(createProjectTaskRequest);

        if (projectTask.getDueDate().getTime() < new Date().getTime())
            throw new DateException(DUE_DATE_CANNOT_BE_BEFORE_NOW);

            Backlog backlog = project.getBacklog();

            int sequence = calculateProjectTaskSequence(backlog);
            backlog.setPTSequence(sequence);
            projectTask.setBacklog(backlog);
            projectTask.setProjectSequence(projectIdentifier + "-" + sequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == null || projectTask.getPriority() == EPriority.NOT_SELECTED.getPriorityCode())
                projectTask.setPriority(EPriority.HIGH.getPriorityCode());

            if (projectTask.getStatus() == null || projectTask.getStatus().isBlank())
                projectTask.setStatus(EStatus.TO_DO.toString());

            projectTaskRepository.save(projectTask);
    }

    public Set<ProjectTaskDto> findProjectBacklog(String projectIdentifier, String username) {
        projectService.findProjectByProjectIdentifier(projectIdentifier, username);
        return projectTaskMapper
                .getAllProjectTasksDto(
                        projectTaskRepository
                                .findProjectTaskByProjectIdentifierOrderByPriority(projectIdentifier));
    }

    public ProjectTaskDto findProjectTaskByProjectTaskSequence(String projectIdentifier, String projectTaskSequence, String username) {
        projectService.findProjectByProjectIdentifier(projectIdentifier, username);
        ProjectTaskDto task = getProjectTaskDto(projectTaskSequence);

        if (!task.getProjectIdentifier().equals(projectIdentifier))
            throw new TaskNotFoundException(PROJECT_TASK_DOESNT_EXISTS_IN_PROJECT + projectIdentifier);

        return task;
    }

    public void update(ProjectTaskDto updatedProjectTask, String projectIdentifier, String projectTaskSequence, String username) {
        ProjectTask task =
                projectTaskMapper
                        .getProjectTask(
                                findProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, username));

        if (updatedProjectTask.getDueDate().getTime() < new Date().getTime())
            throw new DateException(DUE_DATE_CANNOT_BE_BEFORE_NOW);

        task.setAcceptanceCriteria(updatedProjectTask.getAcceptanceCriteria());
        task.setPriority(updatedProjectTask.getPriority());
        task.setStatus(updatedProjectTask.getStatus());
        task.setSummary(updatedProjectTask.getSummary());
        task.setDueDate(updatedProjectTask.getDueDate());
        projectTaskRepository.save(task);
    }

    public boolean delete(String projectIdentifier, String projectTaskSequence, String username) {
        boolean success = false;
        Optional<ProjectTask> task = projectTaskRepository.findProjectTaskByProjectSequence(projectTaskSequence);

        if (task.isPresent() && task.get().getProjectIdentifier().equals(projectIdentifier) &&
                task.get().getBacklog().getProject().getProjectLeader().getUsername().equals(username)) {
            success =true;
            projectTaskRepository.delete(task.get());
        }

        return success;
    }

    private int calculateProjectTaskSequence(Backlog backlog) {
        int backlogSequence = backlog.getPTSequence();
        backlogSequence++;
        return backlogSequence;
    }

    private ProjectTask getProjectTask(String projectTaskSequence) {
        return projectTaskRepository
                .findProjectTaskByProjectSequence(projectTaskSequence)
                .orElseThrow(() -> new TaskNotFoundException(PROJECT_TASK_NOT_FOUND_MSG));
    }

    private ProjectTaskDto getProjectTaskDto(String projectTaskSequence) {
        return projectTaskMapper.getProjectTaskDto(getProjectTask(projectTaskSequence));
    }

}
