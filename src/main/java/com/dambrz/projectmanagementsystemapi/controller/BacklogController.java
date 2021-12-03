package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.RequestValidationException;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectTaskDto;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectTaskRequest;
import com.dambrz.projectmanagementsystemapi.payload.response.DeleteOperationResponse;
import com.dambrz.projectmanagementsystemapi.service.ProjectTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlogs")
@CrossOrigin
public class BacklogController {

    private final ProjectTaskService projectTaskService;

    public BacklogController(ProjectTaskService projectTaskService) {
        this.projectTaskService = projectTaskService;
    }

    @PostMapping("/{projectIdentifier}")
    public void addProjectTaskToBacklog(@Valid @RequestBody CreateProjectTaskRequest projectTask,
                                                     BindingResult result,
                                                     @PathVariable String projectIdentifier,
                                                     Principal principal) {
        if (result.hasErrors()) throw new RequestValidationException(result);
        projectTaskService.save(projectIdentifier, projectTask, principal.getName());
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> getProjectBacklog(@PathVariable String projectIdentifier, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findProjectBacklog(projectIdentifier, principal.getName()), HttpStatus.OK);
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
                .update(
                        updatedProjectTask, projectIdentifier, projectTaskSequence, principal.getName());
    }

    @DeleteMapping("/{projectIdentifier}/{projectTaskSequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String projectIdentifier,
                                               @PathVariable String projectTaskSequence,
                                               Principal principal) {

        if (projectTaskService.delete(projectIdentifier, projectTaskSequence, principal.getName()))
            return new ResponseEntity<>(new DeleteOperationResponse(true), HttpStatus.OK);
        else return new ResponseEntity<>(new DeleteOperationResponse(false), HttpStatus.BAD_REQUEST);
    }
}
