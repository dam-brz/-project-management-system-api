package com.dambrz.projectmanagementsystemapi.payload.response;

import java.util.Set;

public class JWTLoginSuccessResponse {

    private final boolean success;
    private final String token;
    private final Set<String> roles;

    public JWTLoginSuccessResponse(boolean success, String token, Set<String> roles) {
        this.success = success;
        this.token = token;
        this.roles = roles;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
