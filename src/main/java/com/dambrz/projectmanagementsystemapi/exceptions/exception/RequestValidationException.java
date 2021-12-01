package com.dambrz.projectmanagementsystemapi.exceptions.exception;

import org.springframework.validation.BindingResult;

public class RequestValidationException extends RuntimeException {

    private final BindingResult result;

    public RequestValidationException(BindingResult result) {
        this.result = result;
    }

    public BindingResult getResult() {
        return result;
    }
}
