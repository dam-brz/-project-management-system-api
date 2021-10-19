package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.service.ProjectService;
import com.dambrz.projectmanagementsystemapi.service.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin
public class ProjectController {

    private final ProjectService projectService;
    private final ValidationErrorService validationErrorService;

    public ProjectController(ProjectService projectService, ValidationErrorService validationErrorService) {
        this.projectService = projectService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        return new ResponseEntity<>(
                projectService.save(project, principal.getName()),
                HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> getProjectByProjectIdentifier(@PathVariable String projectIdentifier, Principal principal) {

        return new ResponseEntity<Project>(
                projectService.findProjectByProjectIdentifier(projectIdentifier, principal.getName()),
                HttpStatus.OK);
    }

    @GetMapping
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectIdentifier}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectIdentifier, Principal principal) {
        projectService.deleteProjectByIdentifier(projectIdentifier, principal);

        return new ResponseEntity<String>("Project with ID: " + projectIdentifier + " was deleted", HttpStatus.OK);
    }

    @PutMapping("/{projectIdentifier}")
    public ResponseEntity<?> updateProject(@PathVariable String projectIdentifier, @Valid @RequestBody Project project, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        return new ResponseEntity<>(
                projectService.updateProject(projectIdentifier, project, principal.getName()),
                HttpStatus.OK);
    }

}
