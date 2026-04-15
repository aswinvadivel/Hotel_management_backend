package com.hotel.management.controller;

import com.hotel.management.dto.*;
import com.hotel.management.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDto>> createBooking(@Valid @RequestBody BookingCreateDto createDto) {
        BookingResponseDto data = bookingService.createBooking(createDto);
        return new ResponseEntity<>(ApiResponse.created("Booking created successfully", data), HttpStatus.CREATED);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<PagedResponseDto<BookingResponseDto>>> getMyBookings(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponseDto<BookingResponseDto> data = bookingService.getMyBookings(status, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Bookings retrieved successfully", data));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> getBookingById(@PathVariable Integer bookingId) {
        BookingResponseDto data = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(ApiResponse.ok("Booking retrieved successfully", data));
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> updateBooking(@PathVariable Integer bookingId,
                                                                          @Valid @RequestBody BookingUpdateDto updateDto) {
        BookingResponseDto data = bookingService.updateBooking(bookingId, updateDto);
        return ResponseEntity.ok(ApiResponse.ok("Booking updated successfully", data));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingResponseDto>> cancelBooking(@PathVariable Integer bookingId,
                                                                          @Valid @RequestBody BookingCancelDto cancelDto) {
        BookingResponseDto data = bookingService.cancelBooking(bookingId, cancelDto);
        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled successfully", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<BookingResponseDto>>> getAllBookings(
            @RequestParam(required = false) Integer hotelId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponseDto<BookingResponseDto> data = bookingService.getAllBookings(hotelId, status, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Bookings retrieved successfully", data));
    }
}
