package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_amenities", indexes = {
        @Index(name = "idx_room_id", columnList = "room_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_room_amenity", columnNames = {"room_id", "amenity_name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

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
@Table(name = "room_amenities", indexes = {
        @Index(name = "idx_room_id", columnList = "room_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_room_amenity", columnNames = {"room_id", "amenity_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "amenity_name", nullable = false, length = 100)
    private String amenityName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
