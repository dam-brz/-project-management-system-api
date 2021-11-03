package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.payload.JWTLoginSuccessResponse;
import com.dambrz.projectmanagementsystemapi.payload.LoginRequest;
import com.dambrz.projectmanagementsystemapi.security.JWTProvider;
import com.dambrz.projectmanagementsystemapi.service.UserService;
import com.dambrz.projectmanagementsystemapi.service.ValidationErrorService;
import com.dambrz.projectmanagementsystemapi.validator.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.TOKEN_PREFIX;

@RestController
@RequestMapping("api/users")
@CrossOrigin
public class UserController {

    private final ValidationErrorService validationErrorService;
    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final JWTProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserController(ValidationErrorService validationErrorService, UserService userService, PasswordValidator passwordValidator, JWTProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.validationErrorService = validationErrorService;
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user , BindingResult result) {

        passwordValidator.validate(user, result);
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        return new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest , BindingResult result) {

        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                        )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }
}
