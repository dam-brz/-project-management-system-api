package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "backlog",
                    "user",
                    "backlog.projectTasks"
            }
    )
    Project findProjectByProjectIdentifier(String projectIdentifier);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "backlog",
                    "user",
                    "backlog.projectTasks"
            }
    )
    Iterable<Project> findAllByProjectLeader(String projectLeaderUsername);
}
