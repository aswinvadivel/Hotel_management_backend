package com.hotel.management.controller;

import com.hotel.management.dto.*;
import com.hotel.management.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<RoomDto>>> getRoomsByHotel(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) Integer occupancy
    ) {
        PagedResponseDto<RoomDto> data = roomService.getRoomsByHotel(hotelId, page, size, roomType, priceMin, priceMax, occupancy);
        return ResponseEntity.ok(ApiResponse.ok("Rooms retrieved successfully", data));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomDto>> getRoomById(@PathVariable Integer hotelId, @PathVariable Integer roomId) {
        RoomDto data = roomService.getRoomById(hotelId, roomId);
        return ResponseEntity.ok(ApiResponse.ok("Room retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomDto>> createRoom(@PathVariable Integer hotelId,
                                                           @Valid @RequestBody RoomCreateUpdateDto createDto) {
        RoomDto data = roomService.createRoom(hotelId, createDto);
        return new ResponseEntity<>(ApiResponse.created("Room created successfully", data), HttpStatus.CREATED);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomDto>> updateRoom(@PathVariable Integer hotelId,
                                                           @PathVariable Integer roomId,
                                                           @Valid @RequestBody RoomCreateUpdateDto updateDto) {
        RoomDto data = roomService.updateRoom(hotelId, roomId, updateDto);
        return ResponseEntity.ok(ApiResponse.ok("Room updated successfully", data));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable Integer hotelId, @PathVariable Integer roomId) {
        roomService.deleteRoom(hotelId, roomId);
        return new ResponseEntity<>(ApiResponse.ok("Room deleted successfully", null), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{roomId}/availability")
    public ResponseEntity<ApiResponse<RoomAvailabilityDto>> checkAvailability(@PathVariable Integer hotelId,
                                                                               @PathVariable Integer roomId,
                                                                               @RequestParam String checkInDate,
                                                                               @RequestParam String checkOutDate) {
        RoomAvailabilityDto data = roomService.checkAvailability(hotelId, roomId, checkInDate, checkOutDate);
        return ResponseEntity.ok(ApiResponse.ok("Availability checked successfully", data));
    }
}
