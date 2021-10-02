package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
}
