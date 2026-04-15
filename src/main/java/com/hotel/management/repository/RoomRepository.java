package com.hotel.management.repository;

import com.hotel.management.model.Room;
import com.hotel.management.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Page<Room> findByHotelId(Integer hotelId, Pageable pageable);

    Optional<Room> findByIdAndHotelId(Integer id, Integer hotelId);

    boolean existsByHotelIdAndRoomNumber(Integer hotelId, String roomNumber);

    List<Room> findByHotelId(Integer hotelId);

    Page<Room> findByHotelIdAndRoomTypeAndPricePerNightBetweenAndOccupancyGreaterThanEqual(
            Integer hotelId,
            RoomType roomType,
            BigDecimal min,
            BigDecimal max,
            Integer occupancy,
            Pageable pageable
    );
}
