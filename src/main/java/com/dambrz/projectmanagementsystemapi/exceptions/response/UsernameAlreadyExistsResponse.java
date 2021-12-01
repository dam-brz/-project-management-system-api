package com.dambrz.projectmanagementsystemapi.exceptions.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UsernameAlreadyExistsResponse extends ResponseEntity {

    public UsernameAlreadyExistsResponse(Object body, HttpStatus status) {
        super(body, status);
    }
}
