package com.hotel.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAvailabilityDto {
    private Integer roomId;
    private Boolean isAvailable;
    private String checkInDate;
    private String checkOutDate;
}
