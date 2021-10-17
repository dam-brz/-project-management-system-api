package com.dambrz.projectmanagementsystemapi.service;

import com.dambrz.projectmanagementsystemapi.exceptions.UsernameAlreadyExistsError;
import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User saveUser (User user) {
        try {
            user.setUsername(user.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return  userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsError("Username '" + user.getUsername() + "' already exists.");
        }

    }
}
