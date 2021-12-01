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
}
