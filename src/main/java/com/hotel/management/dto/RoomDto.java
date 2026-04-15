package com.hotel.management.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    private Integer id;
    private String roomNumber;
    private String roomType;
    private BigDecimal pricePerNight;
    private Integer occupancy;
    private List<String> amenities;
    private Boolean isAvailable;
    private List<String> images;
    private String description;
}
