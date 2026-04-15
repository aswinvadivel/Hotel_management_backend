package com.hotel.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponseDto {
    private String token;
    private Integer userId;
    private String firstName;
    private String userRole;
}
