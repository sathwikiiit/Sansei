package com.sansei.shop.controller;

import com.sansei.shop.model.ApiUser;
import com.sansei.shop.model.Cart;
import com.sansei.shop.repository.CartRepository;
import com.sansei.shop.service.ApiUserService;
import com.sansei.shop.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final ApiUserService apiUserService;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;

    @Autowired
    public AuthController(ApiUserService apiUserService, JwtUtil jwtUtil, CartRepository cartRepository) {
        this.apiUserService = apiUserService;
        this.jwtUtil = jwtUtil;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to Sansei Shop");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            apiUserService.createDefaultAdmin(); // Ensure default admin is created
            ApiUser  existingUser  = apiUserService.findByUsername(username);
            if (existingUser  == null || !apiUserService.validatePassword(existingUser , password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            String token = jwtUtil.generateToken(existingUser .getUsername(), existingUser .getRole());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody ApiUser  apiUser ) {
        try {
            ApiUser  existingUser  = apiUserService.findByUsername(apiUser .getUsername());
            if (existingUser  != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            } else {
                apiUser .setRole("USER");
                apiUserService.save(apiUser );
                Cart cart = new Cart();
                cart.setUser (apiUser );
                cartRepository.save(cart);
                return ResponseEntity.ok("User  created successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
