package com.example.security.service;

import com.example.security.repository.UserRepository;
import com.example.security.repository.dto.UserDTO;
import com.example.security.repository.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO addUser(String username, String email) {
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            throw new RuntimeException("User already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
        return new UserDTO(username, email);
    }
}
