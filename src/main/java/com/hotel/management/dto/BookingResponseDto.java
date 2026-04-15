package com.hotel.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private Integer id;
    private String bookingNumber;
    private Integer userId;
    private Integer hotelId;
    private Integer roomId;
    private String hotelName;
    private String roomNumber;
    private String checkInDate;
    private String checkOutDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;
    private String status;
    private String specialRequests;
    private String createdAt;
}
