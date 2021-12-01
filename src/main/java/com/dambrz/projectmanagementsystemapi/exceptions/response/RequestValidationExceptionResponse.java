package com.dambrz.projectmanagementsystemapi.exceptions.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RequestValidationExceptionResponse extends ResponseEntity {

    public RequestValidationExceptionResponse(Object body, HttpStatus status) {
        super(body, status);
    }
}
