package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
    Set<ProjectTask> findProjectTaskByProjectIdentifierOrderByPriority(String projectIdentifier);
    ProjectTask findProjectTaskByProjectSequence(String sequence);
}
