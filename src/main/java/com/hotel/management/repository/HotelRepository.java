package com.hotel.management.repository;

import com.hotel.management.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    Page<Hotel> findByCityContainingIgnoreCase(String city, Pageable pageable);
}
