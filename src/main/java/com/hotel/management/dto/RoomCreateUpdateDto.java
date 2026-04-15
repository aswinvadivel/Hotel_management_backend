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
public class RoomCreateUpdateDto {
    @NotBlank
    private String roomNumber;

    @NotBlank
    private String roomType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerNight;

    @NotNull
    @Min(1)
    private Integer occupancy;

    private String description;
    private List<String> amenities;
    private List<String> images;
}
