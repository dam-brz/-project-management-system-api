package com.dambrz.projectmanagementsystemapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsError extends RuntimeException{

    public UsernameAlreadyExistsError(String message) {
        super(message);
    }
}
