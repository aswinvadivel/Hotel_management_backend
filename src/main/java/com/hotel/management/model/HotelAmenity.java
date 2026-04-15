package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_amenities", indexes = {
        @Index(name = "idx_hotel_id", columnList = "hotel_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_hotel_amenity", columnNames = {"hotel_id", "amenity_name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "amenity_name", nullable = false, length = 100)
    private String amenityName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_amenities", indexes = {
        @Index(name = "idx_hotel_id", columnList = "hotel_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_hotel_amenity", columnNames = {"hotel_id", "amenity_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "amenity_name", nullable = false, length = 100)
    private String amenityName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
