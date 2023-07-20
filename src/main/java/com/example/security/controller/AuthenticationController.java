package com.example.security.controller;

import com.example.security.repository.dto.UserDTO;
import com.example.security.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@RestController
public class AuthenticationController {

    private static final String JWT_SECRET = "your-secret-key";
    private static final long JWT_EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // Inject your user service and other dependencies
     private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "email") String email) {
        // Implement user registration logic and save the user to the database
         userService.addUser(username, email);
        // Return appropriate response status
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        // Implement user authentication logic (e.g., check if the credentials are valid)
        // boolean isAuthenticated = userService.authenticate(user.getUsername(), user.getPassword());

        // For simplicity, assume the user is authenticated
        boolean isAuthenticated = true;

        if (isAuthenticated) {
            // Generate JWT token
            String token = generateJwtToken(userDTO.getUsername());

            // Set the JWT token as an HTTP-only cookie
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setMaxAge((int) (JWT_EXPIRATION_TIME / 1000));
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true);
            response.addCookie(jwtCookie);

            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    private String generateJwtToken(String username) {
        byte[] secretKeyBytes = Utf8.encode(JWT_SECRET);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKeyBytes)
                .compact();
    }
}
