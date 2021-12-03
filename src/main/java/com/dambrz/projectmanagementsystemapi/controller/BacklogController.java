package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.RequestValidationException;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectTaskDto;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectTaskRequest;
import com.dambrz.projectmanagementsystemapi.payload.response.DeleteOperationResponse;
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
    public void addProjectTaskToBacklog(@Valid @RequestBody CreateProjectTaskRequest projectTask,
                                                     BindingResult result,
                                                     @PathVariable String projectIdentifier,
                                                     Principal principal) {
        if (result.hasErrors()) throw new RequestValidationException(result);
        projectTaskService.addProjectTask(projectIdentifier, projectTask, principal.getName());
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> getProjectBacklog(@PathVariable String projectIdentifier, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findBacklogByProjectIdentifier(projectIdentifier, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String projectIdentifier,
                                            @PathVariable String projectTaskSequence,
                                            Principal principal) {
        return new ResponseEntity<>(projectTaskService.findProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/{projectIdentifier}/{projectTaskSequence}")
    public void updateProjectTask(@Valid @RequestBody ProjectTaskDto updatedProjectTask,
                                               BindingResult result,
                                               @PathVariable String projectIdentifier,
                                               @PathVariable String projectTaskSequence,
                                               Principal principal) {
        if (result.hasErrors()) throw new RequestValidationException(result);
        projectTaskService
                .updateProjectTask(
                        updatedProjectTask, projectIdentifier, projectTaskSequence, principal.getName());
    }

    @DeleteMapping("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String projectIdentifier,
                                               @PathVariable String projectTaskSequence,
                                               Principal principal) {

        if (projectTaskService.deleteProjectTaskByProjectTaskSequence(projectIdentifier, projectTaskSequence, principal.getName()))
            return new ResponseEntity<>(new DeleteOperationResponse(true), HttpStatus.OK);
        else return new ResponseEntity<>(new DeleteOperationResponse(false), HttpStatus.BAD_REQUEST);
    }
}
