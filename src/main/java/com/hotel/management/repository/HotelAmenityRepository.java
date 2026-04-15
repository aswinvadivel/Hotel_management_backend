package com.hotel.management.repository;

import com.hotel.management.model.HotelAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelAmenityRepository extends JpaRepository<HotelAmenity, Integer> {
    List<HotelAmenity> findByHotelId(Integer hotelId);

    void deleteByHotelId(Integer hotelId);
}
