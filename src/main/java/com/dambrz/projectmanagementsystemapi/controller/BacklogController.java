package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.service.ProjectTaskService;
import com.dambrz.projectmanagementsystemapi.service.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
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
                                                     @PathVariable String projectIdentifier,
                                                     Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask task = projectTaskService.addProjectTask(projectIdentifier, projectTask, principal.getName());

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public Set<ProjectTask> getProjectBacklog(@PathVariable String projectIdentifier,
                                              Principal principal) {

        return projectTaskService.findBacklogByProjectIdentifier(projectIdentifier, principal.getName());
    }

    @GetMapping("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String projectIdentifier,
                                            @PathVariable String projectTaskSequence,
                                            Principal principal) {

        ProjectTask task = projectTaskService.findProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, principal.getName());
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedProjectTask,
                                               BindingResult result,
                                               @PathVariable String projectIdentifier,
                                               @PathVariable String projectTaskSequence,
                                               Principal principal) {

        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask projectTask = projectTaskService.updateProjectTask(updatedProjectTask, projectIdentifier, projectTaskSequence, principal.getName());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String projectIdentifier,
                                               @PathVariable String projectTaskSequence,
                                               Principal principal) {

        projectTaskService.deleteProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, principal.getName());
        return new ResponseEntity<>("Project Task with ID: " + projectTaskSequence + " was deleted", HttpStatus.OK);
    }
}
