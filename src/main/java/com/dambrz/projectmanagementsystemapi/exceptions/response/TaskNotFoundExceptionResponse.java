package com.dambrz.projectmanagementsystemapi.exceptions.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TaskNotFoundExceptionResponse extends ResponseEntity {

    public TaskNotFoundExceptionResponse(Object body, HttpStatus status) {
        super(body, status);
    }
}
