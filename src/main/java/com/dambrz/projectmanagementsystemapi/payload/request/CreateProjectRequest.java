package com.dambrz.projectmanagementsystemapi.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    private final String projectName;

    @NotBlank(message = "Project identifier is required")
    @Size(min = 4, max = 10, message = "Use 4 to 10 characters")
    @Column(unique = true, updatable = false)
    private final String projectIdentifier;

    @NotBlank(message = "Project description is required")
    private final String description;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    public CreateProjectRequest(String projectName, String projectIdentifier, String description, Date startDate, Date endDate) {
        this.projectName = projectName;
        this.projectIdentifier = projectIdentifier;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
