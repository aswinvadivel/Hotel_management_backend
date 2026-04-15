package com.hotel.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCancelDto {
    @NotBlank
    private String cancellationReason;
}
