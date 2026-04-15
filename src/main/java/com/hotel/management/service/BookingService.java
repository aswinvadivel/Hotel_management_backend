package com.hotel.management.service;

import com.hotel.management.dto.*;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.exception.UnauthorizedException;
import com.hotel.management.model.*;
import com.hotel.management.repository.*;
import com.hotel.management.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public BookingResponseDto createBooking(BookingCreateDto createDto) {
        User currentUser = getCurrentUser();
        Hotel hotel = hotelRepository.findById(createDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        Room room = roomRepository.findByIdAndHotelId(createDto.getRoomId(), createDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        LocalDate checkIn = LocalDate.parse(createDto.getCheckInDate());
        LocalDate checkOut = LocalDate.parse(createDto.getCheckOutDate());
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        boolean overlaps = bookingRepository.existsByRoomIdAndStatusInAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                room.getId(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED),
                checkOut,
                checkIn
        );
        if (overlaps) {
            throw new BadRequestException("Room is not available for the selected dates");
        }

        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = Booking.builder()
                .bookingNumber("BK-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8))
                .user(currentUser)
                .hotel(hotel)
                .room(room)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .numberOfGuests(createDto.getNumberOfGuests())
                .totalPrice(totalPrice)
                .status(BookingStatus.CONFIRMED)
                .specialRequests(createDto.getSpecialRequests())
                .build();

        return toBookingResponse(bookingRepository.save(booking));
    }

    public PagedResponseDto<BookingResponseDto> getMyBookings(String status, int page, int size) {
        User currentUser = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Booking> bookings;
        if (status == null || status.isBlank() || status.equalsIgnoreCase("ALL")) {
            bookings = bookingRepository.findByUserId(currentUser.getId(), pageable);
        } else {
            bookings = bookingRepository.findByUserIdAndStatus(currentUser.getId(), BookingStatus.valueOf(status.toUpperCase()), pageable);
        }

        return toPagedResponse(bookings, page, size);
    }

    public BookingResponseDto getBookingById(Integer bookingId) {
        User currentUser = getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!isAdmin(currentUser) && !booking.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Access denied");
        }
        return toBookingResponse(booking);
    }

    public BookingResponseDto updateBooking(Integer bookingId, BookingUpdateDto updateDto) {
        User currentUser = getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new UnauthorizedException("Access denied");
        }

        LocalDate checkIn = LocalDate.parse(updateDto.getCheckInDate());
        LocalDate checkOut = LocalDate.parse(updateDto.getCheckOutDate());
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setNumberOfGuests(updateDto.getNumberOfGuests());
        booking.setSpecialRequests(updateDto.getSpecialRequests());
        booking.setTotalPrice(booking.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(nights)));

        return toBookingResponse(bookingRepository.save(booking));
    }

    public BookingResponseDto cancelBooking(Integer bookingId, BookingCancelDto cancelDto) {
        User currentUser = getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new UnauthorizedException("Access denied");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(java.time.LocalDateTime.now());
        String reason = cancelDto.getCancellationReason();
        booking.setSpecialRequests((booking.getSpecialRequests() == null ? "" : booking.getSpecialRequests() + " | ") + "Cancelled: " + reason);

        return toBookingResponse(bookingRepository.save(booking));
    }

    public PagedResponseDto<BookingResponseDto> getAllBookings(Integer hotelId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Booking> bookings;

        if (hotelId != null && status != null && !status.isBlank()) {
            bookings = bookingRepository.findByHotelIdAndStatus(hotelId, BookingStatus.valueOf(status.toUpperCase()), pageable);
        } else if (hotelId != null) {
            bookings = bookingRepository.findByHotelId(hotelId, pageable);
        } else if (status != null && !status.isBlank()) {
            bookings = bookingRepository.findByStatus(BookingStatus.valueOf(status.toUpperCase()), pageable);
        } else {
            bookings = bookingRepository.findAll(pageable);
        }

        return toPagedResponse(bookings, page, size);
    }

    private PagedResponseDto<BookingResponseDto> toPagedResponse(Page<Booking> bookings, int page, int size) {
        return PagedResponseDto.<BookingResponseDto>builder()
                .content(bookings.getContent().stream().map(this::toBookingResponse).toList())
                .totalElements(bookings.getTotalElements())
                .totalPages(bookings.getTotalPages())
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    private BookingResponseDto toBookingResponse(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .userId(booking.getUser().getId())
                .hotelId(booking.getHotel().getId())
                .roomId(booking.getRoom().getId())
                .hotelName(booking.getHotel().getName())
                .roomNumber(booking.getRoom().getRoomNumber())
                .checkInDate(booking.getCheckInDate().toString())
                .checkOutDate(booking.getCheckOutDate().toString())
                .numberOfGuests(booking.getNumberOfGuests())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .specialRequests(booking.getSpecialRequests())
                .createdAt(booking.getCreatedAt() == null ? null : booking.getCreatedAt().toString())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedException("Unauthorized");
        }
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private boolean isAdmin(User user) {
        return user.getUserRole() == UserRole.ADMIN;
    }
}
