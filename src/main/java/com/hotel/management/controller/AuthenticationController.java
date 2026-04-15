package com.hotel.management.controller;

import com.hotel.management.dto.*;
import com.hotel.management.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserRegisterDto registerDto) {
        UserResponseDto data = authenticationService.register(registerDto);
        return new ResponseEntity<>(ApiResponse.created("User registered successfully", data), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> login(@Valid @RequestBody UserLoginDto loginDto) {
        UserLoginResponseDto data = authenticationService.login(loginDto);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", data));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", null));
    }
}
