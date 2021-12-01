package com.dambrz.projectmanagementsystemapi.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegistrationRequest {

    @Email(message = "Username needs to be an email")
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Please enter full name")
    private final String fullName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Use min 6 characters")
    private final String password;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Use min 6 characters")
    private final String confirmPassword;

    public RegistrationRequest(String username, String fullName, String password, String confirmPassword) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
