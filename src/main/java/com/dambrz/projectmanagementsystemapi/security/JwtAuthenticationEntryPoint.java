package com.dambrz.projectmanagementsystemapi.security;

import com.dambrz.projectmanagementsystemapi.exceptions.response.InvalidLoginResponse;
import com.google.gson.Gson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String APPLICATION_JSON = "application/json";
    public static final int UNAUTHORIZED = 401;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        InvalidLoginResponse loginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(loginResponse);

        httpServletResponse.setContentType(APPLICATION_JSON);
        httpServletResponse.setStatus(UNAUTHORIZED);
        httpServletResponse.getWriter().print(jsonLoginResponse);
    }
}
