package com.hotel.management.repository;

import com.hotel.management.model.Booking;
import com.hotel.management.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByBookingNumber(String bookingNumber);

    Page<Booking> findByUserId(Integer userId, Pageable pageable);

    Page<Booking> findByUserIdAndStatus(Integer userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByHotelId(Integer hotelId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Page<Booking> findByHotelIdAndStatus(Integer hotelId, BookingStatus status, Pageable pageable);

    boolean existsByRoomIdAndStatusInAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Integer roomId,
            List<BookingStatus> statuses,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );
}
