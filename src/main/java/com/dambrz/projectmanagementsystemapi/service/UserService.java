package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
