package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.exception.UsernameAlreadyExistsError;
import com.dambrz.projectmanagementsystemapi.mapper.UserMapper;
import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.model.enums.ERole;
import com.dambrz.projectmanagementsystemapi.payload.request.RegistrationRequest;
import com.dambrz.projectmanagementsystemapi.repository.RoleRepository;
import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    public User saveUser (RegistrationRequest registrationRequest) {
        if (isUsernameExists(registrationRequest.getUsername())) {
            throw new UsernameAlreadyExistsError("Username '" + registrationRequest.getUsername() + "' already exists.");
        } else {
            User user = getUserFromRegistrationRequest(registrationRequest);
            user.setRoles(Collections.singleton(roleRepository.findByName(ERole.ROLE_DEVELOPER)));
            return userRepository.save(user);
        }
    }

    private boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private User getUserFromRegistrationRequest(RegistrationRequest registrationRequest) {
        return userMapper.getUserFromRegistrationRequest(registrationRequest);
    }
}
