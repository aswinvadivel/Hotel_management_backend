package com.hotel.management.repository;

import com.hotel.management.model.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {
    List<RoomImage> findByRoomId(Integer roomId);

    void deleteByRoomId(Integer roomId);
}
