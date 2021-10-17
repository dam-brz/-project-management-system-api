package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findProjectByProjectIdentifier(String projectIdentifier);

    @Override
    Iterable<Project> findAll();

    Iterable<Project> findAllByProjectLeader(String projectLeaderUsername);
}
