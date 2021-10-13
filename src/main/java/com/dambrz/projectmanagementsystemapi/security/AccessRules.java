package com.dambrz.projectmanagementsystemapi.security;

public class AccessRules {

    protected static final String[] STATIC_CONTENT = {
            "/",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
    };

    protected static final String[] FOR_EVERYONE = {
            "/api/users/**",
            "/h2-console/**"
    };
}
