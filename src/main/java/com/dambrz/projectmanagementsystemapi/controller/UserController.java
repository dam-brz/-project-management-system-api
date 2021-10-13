package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.service.UserService;
import com.dambrz.projectmanagementsystemapi.service.ValidationErrorService;
import com.dambrz.projectmanagementsystemapi.validator.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.dambrz.projectmanagementsystemapi.model.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final ValidationErrorService validationErrorService;
    private final UserService userService;
    private final PasswordValidator passwordValidator;

    public UserController(ValidationErrorService validationErrorService, UserService userService, PasswordValidator userValidator) {
        this.validationErrorService = validationErrorService;
        this.userService = userService;
        this.passwordValidator = userValidator;
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
}
