package com.dambrz.projectmanagementsystemapi.exceptions.response;

public class HttpMessageNotReadableExceptionResponse {

    private String date;

    public HttpMessageNotReadableExceptionResponse(String message) {
        this.date = message;
    }

    public String getMessage() {
        return date;
    }
}
