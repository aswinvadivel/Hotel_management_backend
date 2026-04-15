package com.hotel.management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelCreateUpdateDto {
    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

    @Email
    private String email;

    private String description;
    private String image;
    private BigDecimal rating;
    private Integer totalReviews;
    private List<String> amenities;
}
