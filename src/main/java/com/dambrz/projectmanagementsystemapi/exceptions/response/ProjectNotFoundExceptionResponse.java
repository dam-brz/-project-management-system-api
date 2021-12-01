package com.dambrz.projectmanagementsystemapi.exceptions.response;

public class ProjectNotFoundExceptionResponse {

    private String projectIdentifier;

    public ProjectNotFoundExceptionResponse(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
