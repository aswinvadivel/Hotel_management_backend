package com.hotel.management.service;

import com.hotel.management.dto.UserLoginDto;
import com.hotel.management.dto.UserLoginResponseDto;
import com.hotel.management.dto.UserRegisterDto;
import com.hotel.management.dto.UserResponseDto;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.model.User;
import com.hotel.management.model.UserRole;
import com.hotel.management.repository.UserRepository;
import com.hotel.management.security.JwtTokenProvider;
import com.hotel.management.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserResponseDto register(UserRegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        UserRole role = UserRole.GUEST;
        if (registerDto.getUserRole() != null && !registerDto.getUserRole().isBlank()) {
            role = UserRole.valueOf(registerDto.getUserRole().trim().toUpperCase());
        }

        User user = User.builder()
                .email(registerDto.getEmail())
                .passwordHash(passwordEncoder.encode(registerDto.getPassword()))
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .phoneNumber(registerDto.getPhoneNumber())
                .userRole(role)
                .isActive(true)
                .build();

        User saved = userRepository.save(user);
        return UserResponseDto.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .phoneNumber(saved.getPhoneNumber())
                .userRole(saved.getUserRole().name())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public UserLoginResponseDto login(UserLoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);

        return UserLoginResponseDto.builder()
                .token(token)
                .userId(principal.getId())
                .firstName(userRepository.findById(principal.getId()).map(User::getFirstName).orElse(""))
                .userRole(principal.getRole())
                .build();
    }
}
