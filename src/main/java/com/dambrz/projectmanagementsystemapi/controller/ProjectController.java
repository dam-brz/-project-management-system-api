package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.RequestValidationException;
import com.dambrz.projectmanagementsystemapi.payload.dto.ProjectDto;
import com.dambrz.projectmanagementsystemapi.payload.request.CreateProjectRequest;
import com.dambrz.projectmanagementsystemapi.payload.response.DeleteOperationResponse;
import com.dambrz.projectmanagementsystemapi.service.ProjectService;
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

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public void createNewProject(@Valid @RequestBody CreateProjectRequest project, BindingResult result, Principal principal) {
        if (result.hasErrors()) throw new RequestValidationException(result);
        projectService.save(project, principal.getName());
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> getProjectByProjectIdentifier(@PathVariable String projectIdentifier, Principal principal) {
        return new ResponseEntity<>(
                projectService.findProjectByProjectIdentifier(projectIdentifier, principal.getName()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(Principal principal) {
        return new ResponseEntity<>(projectService.findAllProjectsDto(principal), HttpStatus.OK);
    }

    @DeleteMapping("/{projectIdentifier}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectIdentifier, Principal principal) {
        if (projectService.deleteProjectByIdentifier(projectIdentifier, principal.getName()))
            return new ResponseEntity<>(new DeleteOperationResponse(true), HttpStatus.OK);
        else return new ResponseEntity<>(new DeleteOperationResponse(false), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{projectIdentifier}")
    public void updateProject(@PathVariable String projectIdentifier, @Valid @RequestBody ProjectDto projectDto, BindingResult result, Principal principal) {
        if (result.hasErrors()) throw new RequestValidationException(result);
        projectService.updateProject(projectIdentifier, projectDto, principal.getName());
    }

}
