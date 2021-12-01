package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "roles",
                    "projects",
                    "projects.backlog"
            }
    )
    User findByUsername(String username);

    User getById(Long id);

    Boolean existsByUsername(String username);
}
