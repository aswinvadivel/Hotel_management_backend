package com.hotel.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels", indexes = {
        @Index(name = "idx_city", columnList = "city"),
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_rating", columnList = "rating")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HotelAmenity> amenities = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (rating == null) {
            rating = BigDecimal.ZERO;
        }
        if (totalReviews == null) {
            totalReviews = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
