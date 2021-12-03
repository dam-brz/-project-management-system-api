package com.dambrz.projectmanagementsystemapi.mapper;

import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectTaskDto;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectTaskRequest;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectTaskMapper {

    public ProjectTask getProjectTask(CreateProjectTaskRequest createProjectTaskRequest) {
        return new ProjectTask(
                createProjectTaskRequest.getSummary(),
                createProjectTaskRequest.getAcceptanceCriteria(),
                createProjectTaskRequest.getStatus(),
                createProjectTaskRequest.getPriority(),
                createProjectTaskRequest.getDueDate());
    }

    public ProjectTask getProjectTask(ProjectTaskDto projectTask) {
        return new ProjectTask(
                projectTask.getId(),
                projectTask.getProjectSequence(),
                projectTask.getSummary(),
                projectTask.getAcceptanceCriteria(),
                projectTask.getStatus(),
                projectTask.getPriority(),
                projectTask.getDueDate(),
                projectTask.getProjectIdentifier());
    }

    public ProjectTaskDto getProjectTaskDto(ProjectTask projectTask) {
        return new ProjectTaskDto(
                projectTask.getId(),
                projectTask.getProjectSequence(),
                projectTask.getSummary(),
                projectTask.getAcceptanceCriteria(),
                projectTask.getStatus(),
                projectTask.getPriority(),
                projectTask.getDueDate(),
                projectTask.getProjectIdentifier());
    }

    public Set<ProjectTaskDto> getAllProjectTasksDto(Set<ProjectTask> projectTasks) {
        return projectTasks.stream()
                .map(this::getProjectTaskDto)
                .sorted(Comparator.comparingInt(ProjectTaskDto::getPriority))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
