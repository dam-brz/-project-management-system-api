package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.RequestValidationException;
import com.dambrz.projectmanagementsystemapi.payload.request.RegistrationRequest;
import com.dambrz.projectmanagementsystemapi.payload.response.JWTLoginSuccessResponse;
import com.dambrz.projectmanagementsystemapi.payload.request.LoginRequest;
import com.dambrz.projectmanagementsystemapi.security.JWTProvider;
import com.dambrz.projectmanagementsystemapi.service.UserService;
import com.dambrz.projectmanagementsystemapi.validator.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Set;
import java.util.stream.Collectors;

import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.TOKEN_PREFIX;

@RestController
@RequestMapping("api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final JWTProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, PasswordValidator passwordValidator, JWTProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest registrationRequest , BindingResult result) {

        passwordValidator.validate(registrationRequest, result);
        if (result.hasErrors()) throw new RequestValidationException(result);
        return new ResponseEntity<>(userService.saveUser(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest , BindingResult result) {

        if (result.hasErrors()) throw new RequestValidationException(result);

        Authentication authentication = getAuthentication(loginRequest);
        String jwt = generateJwt(authentication);
        Set<String> roles = getRoles(authentication);

        return new ResponseEntity<>(new JWTLoginSuccessResponse(true, jwt, roles), HttpStatus.OK);
    }

    private Authentication getAuthentication(LoginRequest loginRequest) {
        return authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    private String generateJwt(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return TOKEN_PREFIX + tokenProvider.generateToken(authentication);
    }

    private Set<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
