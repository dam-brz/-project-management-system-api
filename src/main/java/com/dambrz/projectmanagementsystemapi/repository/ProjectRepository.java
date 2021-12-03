package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "backlog",
                    "projectLeader",
                    "backlog.projectTasks"
            }
    )
    Optional<Project> findByProjectIdentifier(String projectIdentifier);

    Boolean existsByProjectIdentifier(String projectIdentifier);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "backlog",
                    "projectLeader",
                    "backlog.projectTasks"
            }
    )
    List<Project> findAllByProjectLeader(User projectLeader);
}
