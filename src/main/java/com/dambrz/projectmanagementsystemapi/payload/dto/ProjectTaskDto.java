package com.dambrz.projectmanagementsystemapi.payload.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ProjectTaskDto {

    private Long id;
    private String projectSequence;

    @NotBlank(message = "Summary is required")
    private String summary;

    @NotBlank(message = "Acceptance criteria is required")
    private String acceptanceCriteria;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Priority is required")
    private Integer priority;

    @NotNull(message = "Due date is required")
    private Date dueDate;

    private String projectIdentifier;

    public ProjectTaskDto(Long id, String projectSequence, String summary, String acceptanceCriteria, String status, Integer priority, Date dueDate, String projectIdentifier) {
        this.id = id;
        this.projectSequence = projectSequence;
        this.summary = summary;
        this.acceptanceCriteria = acceptanceCriteria;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectIdentifier = projectIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectSequence() {
        return projectSequence;
    }

    public void setProjectSequence(String projectSequence) {
        this.projectSequence = projectSequence;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
