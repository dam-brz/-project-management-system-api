package com.dambrz.projectmanagementsystemapi.payload.request;

import com.dambrz.projectmanagementsystemapi.validation.annotation.Password;
import com.dambrz.projectmanagementsystemapi.validation.annotation.PasswordValueMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@PasswordValueMatch(field = "password", fieldMatch = "confirmPassword")
public class RegistrationRequest {

    @Email(message = "Username needs to be an email")
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Please enter full name")
    private final String fullName;

    @Password
    private final String password;

    @Password
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
