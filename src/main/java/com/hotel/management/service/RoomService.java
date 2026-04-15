package com.hotel.management.service;

import com.hotel.management.dto.*;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.*;
import com.hotel.management.repository.BookingRepository;
import com.hotel.management.repository.HotelRepository;
import com.hotel.management.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    public PagedResponseDto<RoomDto> getRoomsByHotel(Integer hotelId, int page, int size, String roomType, BigDecimal priceMin, BigDecimal priceMax, Integer occupancy) {
        hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Room> all = roomRepository.findByHotelId(hotelId);

        List<Room> filtered = all.stream().filter(room -> {
            boolean ok = true;
            if (roomType != null && !roomType.isBlank()) {
                ok &= room.getRoomType().name().equalsIgnoreCase(roomType);
            }
            if (priceMin != null) {
                ok &= room.getPricePerNight().compareTo(priceMin) >= 0;
            }
            if (priceMax != null) {
                ok &= room.getPricePerNight().compareTo(priceMax) <= 0;
            }
            if (occupancy != null) {
                ok &= room.getOccupancy() >= occupancy;
            }
            return ok;
        }).toList();

        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        List<RoomDto> content = filtered.subList(start, end).stream().map(this::toRoomDto).toList();

        return PagedResponseDto.<RoomDto>builder()
                .content(content)
                .totalElements(filtered.size())
                .totalPages((int) Math.ceil((double) filtered.size() / size))
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    public RoomDto getRoomById(Integer hotelId, Integer roomId) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return toRoomDto(room);
    }

    @Transactional
    public RoomDto createRoom(Integer hotelId, RoomCreateUpdateDto createDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        if (roomRepository.existsByHotelIdAndRoomNumber(hotelId, createDto.getRoomNumber())) {
            throw new BadRequestException("Room number already exists in this hotel");
        }

        Room room = Room.builder()
                .hotel(hotel)
                .roomNumber(createDto.getRoomNumber())
                .roomType(RoomType.valueOf(createDto.getRoomType().toUpperCase()))
                .pricePerNight(createDto.getPricePerNight())
                .occupancy(createDto.getOccupancy())
                .description(createDto.getDescription())
                .isAvailable(true)
                .build();

        if (createDto.getAmenities() != null) {
            room.setAmenities(createDto.getAmenities().stream()
                    .map(a -> RoomAmenity.builder().room(room).amenityName(a).build())
                    .toList());
        }
        if (createDto.getImages() != null) {
            room.setImages(createDto.getImages().stream()
                    .map(i -> RoomImage.builder().room(room).imageUrl(i).isPrimary(false).build())
                    .toList());
        }

        Room saved = roomRepository.save(room);
        return toRoomDto(saved);
    }

    @Transactional
    public RoomDto updateRoom(Integer hotelId, Integer roomId, RoomCreateUpdateDto updateDto) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setRoomNumber(updateDto.getRoomNumber());
        room.setRoomType(RoomType.valueOf(updateDto.getRoomType().toUpperCase()));
        room.setPricePerNight(updateDto.getPricePerNight());
        room.setOccupancy(updateDto.getOccupancy());
        room.setDescription(updateDto.getDescription());

        room.getAmenities().clear();
        if (updateDto.getAmenities() != null) {
            room.getAmenities().addAll(updateDto.getAmenities().stream()
                    .map(a -> RoomAmenity.builder().room(room).amenityName(a).build())
                    .toList());
        }

        room.getImages().clear();
        if (updateDto.getImages() != null) {
            room.getImages().addAll(updateDto.getImages().stream()
                    .map(i -> RoomImage.builder().room(room).imageUrl(i).isPrimary(false).build())
                    .toList());
        }

        Room saved = roomRepository.save(room);
        return toRoomDto(saved);
    }

    public void deleteRoom(Integer hotelId, Integer roomId) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    public RoomAvailabilityDto checkAvailability(Integer hotelId, Integer roomId, String checkInDate, String checkOutDate) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);

        boolean booked = bookingRepository.existsByRoomIdAndStatusInAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                room.getId(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED),
                checkOut,
                checkIn
        );

        return RoomAvailabilityDto.builder()
                .roomId(room.getId())
                .isAvailable(!booked && Boolean.TRUE.equals(room.getIsAvailable()))
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
    }

    private RoomDto toRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType().name())
                .pricePerNight(room.getPricePerNight())
                .occupancy(room.getOccupancy())
                .amenities(room.getAmenities().stream().map(RoomAmenity::getAmenityName).toList())
                .isAvailable(room.getIsAvailable())
                .images(room.getImages().stream().map(RoomImage::getImageUrl).toList())
                .description(room.getDescription())
                .build();
    }
}
