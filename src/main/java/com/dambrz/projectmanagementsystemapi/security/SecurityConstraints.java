package com.dambrz.projectmanagementsystemapi.security;

public class SecurityConstraints {

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

    protected static final String[] PERMIT_ALL_URLS = {
            "/api/users/**",
            "/h2-console/**"
    };

    protected static final String SECRET = "SecreteKey";
    public static final String TOKEN_PREFIX = "Bearer ";
    protected static final String HEADER_STRING = "Authorization";
    protected static final long EXPIRATION_TIME = 30_000; //TESTS
}
