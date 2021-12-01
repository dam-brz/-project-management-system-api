package com.dambrz.projectmanagementsystemapi.mapper;

import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.payload.request.RegistrationRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserMapper(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserFromRegistrationRequest(RegistrationRequest registrationRequest) {
        return new User(
                registrationRequest.getUsername(),
                registrationRequest.getFullName(),
                bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
    }
}
