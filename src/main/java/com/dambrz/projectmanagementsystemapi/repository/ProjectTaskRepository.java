package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "backlog"
            }
    )
    Set<ProjectTask> findProjectTaskByProjectIdentifierOrderByPriority(String projectIdentifier);

    ProjectTask findProjectTaskByProjectSequence(String sequence);
}
