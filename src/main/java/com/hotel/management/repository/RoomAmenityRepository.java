package com.hotel.management.repository;

import com.hotel.management.model.RoomAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, Integer> {
    List<RoomAmenity> findByRoomId(Integer roomId);

    void deleteByRoomId(Integer roomId);
}
