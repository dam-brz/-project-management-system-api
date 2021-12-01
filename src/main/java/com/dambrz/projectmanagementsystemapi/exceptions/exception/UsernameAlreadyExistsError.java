package com.dambrz.projectmanagementsystemapi.exceptions.exception;

public class UsernameAlreadyExistsError extends RuntimeException{

    public UsernameAlreadyExistsError(String message) {
        super(message);
    }
}
