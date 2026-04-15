package com.hotel.management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
