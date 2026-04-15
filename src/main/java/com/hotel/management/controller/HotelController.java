package com.hotel.management.controller;

import com.hotel.management.dto.*;
import com.hotel.management.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<HotelDto>>> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        PagedResponseDto<HotelDto> data = hotelService.getAllHotels(page, size, city, minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.ok("Hotels retrieved successfully", data));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelDto>> getHotelById(@PathVariable Integer hotelId) {
        HotelDto data = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(ApiResponse.ok("Hotel retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HotelDto>> createHotel(@Valid @RequestBody HotelCreateUpdateDto createDto) {
        HotelDto data = hotelService.createHotel(createDto);
        return new ResponseEntity<>(ApiResponse.created("Hotel created successfully", data), HttpStatus.CREATED);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelDto>> updateHotel(@PathVariable Integer hotelId,
                                                             @Valid @RequestBody HotelCreateUpdateDto updateDto) {
        HotelDto data = hotelService.updateHotel(hotelId, updateDto);
        return ResponseEntity.ok(ApiResponse.ok("Hotel updated successfully", data));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<String>> deleteHotel(@PathVariable Integer hotelId) {
        hotelService.deleteHotel(hotelId);
        return new ResponseEntity<>(ApiResponse.ok("Hotel deleted successfully", null), HttpStatus.NO_CONTENT);
    }
}
