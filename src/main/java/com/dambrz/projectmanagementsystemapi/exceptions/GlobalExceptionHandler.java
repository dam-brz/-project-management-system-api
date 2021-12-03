package com.dambrz.projectmanagementsystemapi.exceptions;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.*;
import com.dambrz.projectmanagementsystemapi.exceptions.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<?> handleProjectIdException(ProjectIdException projectIdException, WebRequest request) {
        ProjectIdExceptionResponse projectIdExceptionResponse = new ProjectIdExceptionResponse(projectIdException.getMessage());
        return new ResponseEntity(projectIdExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleProjectNotFoundException(ProjectNotFoundException projectNotFoundException, WebRequest request) {
        ProjectNotFoundExceptionResponse projectNotFoundExceptionResponse = new ProjectNotFoundExceptionResponse(projectNotFoundException.getMessage());
        return new ResponseEntity(projectNotFoundExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsError usernameAlreadyExistsError, WebRequest request) {
        return new UsernameAlreadyExistsResponse(usernameAlreadyExistsError.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleRequestValidationException(RequestValidationException requestValidationException, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        requestValidationException.getResult().getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return new RequestValidationExceptionResponse(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleTaskNotFoundException(TaskNotFoundException taskNotFoundException, WebRequest request) {
        return new UsernameAlreadyExistsResponse(taskNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
