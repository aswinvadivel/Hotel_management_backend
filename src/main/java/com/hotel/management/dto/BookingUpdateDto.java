package com.hotel.management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingUpdateDto {
    @NotBlank
    private String checkInDate;

    @NotBlank
    private String checkOutDate;

    @NotNull
    @Min(1)
    private Integer numberOfGuests;

    private String specialRequests;
}
