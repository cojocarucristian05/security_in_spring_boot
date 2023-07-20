package com.example.security.service;

import com.example.security.repository.dto.UserDTO;

public interface UserService {
    UserDTO addUser(String username, String email);
}
