package com.hotel.management.service;

import com.hotel.management.dto.*;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Hotel;
import com.hotel.management.model.HotelAmenity;
import com.hotel.management.model.Room;
import com.hotel.management.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;

    public PagedResponseDto<HotelDto> getAllHotels(int page, int size, String city, BigDecimal minPrice, BigDecimal maxPrice) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Hotel> hotels = (city == null || city.isBlank())
                ? hotelRepository.findAll(pageable)
                : hotelRepository.findByCityContainingIgnoreCase(city, pageable);

        List<HotelDto> content = hotels.getContent().stream().map(this::toHotelDtoWithoutRooms).toList();
        return PagedResponseDto.<HotelDto>builder()
                .content(content)
                .totalElements(hotels.getTotalElements())
                .totalPages(hotels.getTotalPages())
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    public HotelDto getHotelById(Integer hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        return toHotelDtoWithRooms(hotel);
    }

    @Transactional
    public HotelDto createHotel(HotelCreateUpdateDto createDto) {
        Hotel hotel = Hotel.builder()
                .name(createDto.getName())
                .city(createDto.getCity())
                .address(createDto.getAddress())
                .phoneNumber(createDto.getPhoneNumber())
                .email(createDto.getEmail())
                .description(createDto.getDescription())
                .imageUrl(createDto.getImage())
                .rating(createDto.getRating())
                .totalReviews(createDto.getTotalReviews())
                .build();

        if (createDto.getAmenities() != null) {
            List<HotelAmenity> amenities = new ArrayList<>();
            for (String amenity : createDto.getAmenities()) {
                amenities.add(HotelAmenity.builder().hotel(hotel).amenityName(amenity).build());
            }
            hotel.setAmenities(amenities);
        }

        Hotel saved = hotelRepository.save(hotel);
        return toHotelDtoWithRooms(saved);
    }

    @Transactional
    public HotelDto updateHotel(Integer hotelId, HotelCreateUpdateDto updateDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        hotel.setName(updateDto.getName());
        hotel.setCity(updateDto.getCity());
        hotel.setAddress(updateDto.getAddress());
        hotel.setPhoneNumber(updateDto.getPhoneNumber());
        hotel.setEmail(updateDto.getEmail());
        hotel.setDescription(updateDto.getDescription());
        hotel.setImageUrl(updateDto.getImage());

        if (updateDto.getAmenities() != null) {
            hotel.getAmenities().clear();
            for (String amenity : updateDto.getAmenities()) {
                hotel.getAmenities().add(HotelAmenity.builder().hotel(hotel).amenityName(amenity).build());
            }
        }

        Hotel saved = hotelRepository.save(hotel);
        return toHotelDtoWithRooms(saved);
    }

    public void deleteHotel(Integer hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotelRepository.delete(hotel);
    }

    private HotelDto toHotelDtoWithoutRooms(Hotel hotel) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .phoneNumber(hotel.getPhoneNumber())
                .email(hotel.getEmail())
                .rating(hotel.getRating())
                .totalReviews(hotel.getTotalReviews())
                .image(hotel.getImageUrl())
                .amenities(hotel.getAmenities().stream().map(HotelAmenity::getAmenityName).toList())
                .description(hotel.getDescription())
                .rooms(List.of())
                .build();
    }

    private HotelDto toHotelDtoWithRooms(Hotel hotel) {
        List<RoomDto> rooms = hotel.getRooms().stream().map(this::toRoomDto).toList();
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .phoneNumber(hotel.getPhoneNumber())
                .email(hotel.getEmail())
                .rating(hotel.getRating())
                .totalReviews(hotel.getTotalReviews())
                .image(hotel.getImageUrl())
                .amenities(hotel.getAmenities().stream().map(HotelAmenity::getAmenityName).toList())
                .description(hotel.getDescription())
                .rooms(rooms)
                .build();
    }

    private RoomDto toRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType().name())
                .pricePerNight(room.getPricePerNight())
                .occupancy(room.getOccupancy())
                .description(room.getDescription())
                .isAvailable(room.getIsAvailable())
                .amenities(room.getAmenities().stream().map(a -> a.getAmenityName()).toList())
                .images(room.getImages().stream().map(i -> i.getImageUrl()).toList())
                .build();
    }
}
