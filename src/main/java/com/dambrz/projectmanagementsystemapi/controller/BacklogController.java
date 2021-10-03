package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.service.ProjectTaskService;
import com.dambrz.projectmanagementsystemapi.service.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/backlogs")
@CrossOrigin
public class BacklogController {

    private final ProjectTaskService projectTaskService;

    private final ValidationErrorService validationErrorService;

    public BacklogController(ProjectTaskService projectTaskService, ValidationErrorService validationErrorService) {
        this.projectTaskService = projectTaskService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("/{projectIdentifier}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result,
                                                     @PathVariable String projectIdentifier) {
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask task = projectTaskService.addProjectTask(projectIdentifier, projectTask);

        return new ResponseEntity<ProjectTask>(task, HttpStatus.CREATED);
    }

    @GetMapping ("/{projectIdentifier}")
    public Set<ProjectTask> getProjectBacklog(@PathVariable String projectIdentifier) {
        return projectTaskService.findBacklogByProjectIdentifier(projectIdentifier);
    }

    @GetMapping ("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String projectIdentifier,
                                            @PathVariable String projectTaskSequence)  {
        return new ResponseEntity<ProjectTask>(projectTaskService.findProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence), HttpStatus.CREATED);
    }

}
