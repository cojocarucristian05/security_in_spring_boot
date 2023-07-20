package com.example.security.controller;

import com.example.security.repository.dto.UserDTO;
import com.example.security.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public UserDTO addUser(@RequestParam(name = "username") String username,
                           @RequestParam(name = "email") String email) {
        return userService.addUser(username, email);
    }
}
