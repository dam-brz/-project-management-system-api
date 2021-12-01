package com.dambrz.projectmanagementsystemapi.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Cannot be blank")
    private String username;

    @NotBlank(message = "Cannot be blank")
    private final String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
