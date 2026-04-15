package com.hotel.management.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {
    private Integer id;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal rating;
    private Integer totalReviews;
    private String image;
    private List<String> amenities;
    private String description;
    private List<RoomDto> rooms;
}
