package com.dambrz.projectmanagementsystemapi.exceptions.response;

public class InvalidLoginResponse {

    private final String credentials;

    public InvalidLoginResponse() {
        this.credentials = "Invalid credentials";
    }
}
