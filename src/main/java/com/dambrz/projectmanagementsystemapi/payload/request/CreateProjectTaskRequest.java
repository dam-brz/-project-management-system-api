package com.dambrz.projectmanagementsystemapi.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class CreateProjectTaskRequest {

    @NotBlank(message = "Summary is required")
    private String summary;

    @NotBlank(message = "Acceptance criteria is required")
    private String acceptanceCriteria;

    private String status;
    private int priority;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Due date is required")
    private Date dueDate;

    public String getSummary() {
        return summary;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public String getStatus() {
        return status;
    }

    public Integer getPriority() {
        return priority;
    }

    public Date getDueDate() {
        return dueDate;
    }
}
