-- ============================================
-- Hotel Management System - MySQL Database
-- ============================================
-- Version: 1.0
-- This script creates the complete database schema with sample data
-- Just run this file once and you're ready to go!
-- ============================================

-- Drop existing database if needed (uncomment to reset)
-- DROP DATABASE IF EXISTS hotel_management;

-- Create database
CREATE DATABASE IF NOT EXISTS hotel_management 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE hotel_management;

-- ============================================
-- 1. USERS TABLE
-- ============================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15),
    user_role ENUM('GUEST', 'ADMIN', 'STAFF') NOT NULL DEFAULT 'GUEST',
    date_of_birth DATE,
    address VARCHAR(255),
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    zip_code VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    INDEX idx_email (email),
    INDEX idx_user_role (user_role),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. HOTELS TABLE
-- ============================================
CREATE TABLE hotels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    email VARCHAR(100),
    description TEXT,
    image_url VARCHAR(500),
    rating DECIMAL(3, 2) DEFAULT 0,
    total_reviews INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    INDEX idx_city (city),
    INDEX idx_name (name),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 3. HOTEL_AMENITIES JUNCTION TABLE
-- ============================================
CREATE TABLE hotel_amenities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    amenity_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
    INDEX idx_hotel_id (hotel_id),
    UNIQUE KEY unique_hotel_amenity (hotel_id, amenity_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 4. ROOMS TABLE
-- ============================================
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    room_type ENUM('SINGLE', 'DOUBLE', 'SUITE', 'DELUXE') NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    occupancy INT NOT NULL,
    description TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
    INDEX idx_hotel_id (hotel_id),
    INDEX idx_room_type (room_type),
    INDEX idx_is_available (is_available),
    UNIQUE KEY unique_hotel_room_number (hotel_id, room_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 5. ROOM_AMENITIES JUNCTION TABLE
-- ============================================
CREATE TABLE room_amenities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    amenity_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    INDEX idx_room_id (room_id),
    UNIQUE KEY unique_room_amenity (room_id, amenity_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 6. ROOM_IMAGES TABLE
-- ============================================
CREATE TABLE room_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    INDEX idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 7. BOOKINGS TABLE
-- ============================================
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_number VARCHAR(50) UNIQUE NOT NULL,
    user_id INT NOT NULL,
    hotel_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_guests INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED', 'COMPLETED', 'PENDING') NOT NULL DEFAULT 'PENDING',
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE RESTRICT,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE RESTRICT,
    INDEX idx_user_id (user_id),
    INDEX idx_hotel_id (hotel_id),
    INDEX idx_room_id (room_id),
    INDEX idx_check_in_date (check_in_date),
    INDEX idx_check_out_date (check_out_date),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_booking_number (booking_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 8. PAYMENTS TABLE
-- ============================================
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'NET_BANKING', 'WALLET') NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    status ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    payment_gateway VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE RESTRICT,
    INDEX idx_booking_id (booking_id),
    INDEX idx_status (status),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 9. REFUNDS TABLE
-- ============================================
CREATE TABLE refunds (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_id INT NOT NULL,
    booking_id INT NOT NULL,
    refund_amount DECIMAL(10, 2) NOT NULL,
    refund_reason VARCHAR(255),
    refund_transaction_id VARCHAR(100) UNIQUE,
    status ENUM('PENDING', 'SUCCESS', 'FAILED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE RESTRICT,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE RESTRICT,
    INDEX idx_payment_id (payment_id),
    INDEX idx_booking_id (booking_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 10. REVIEWS TABLE
-- ============================================
CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    hotel_id INT NOT NULL,
    user_id INT NOT NULL,
    rating DECIMAL(3, 2) NOT NULL,
    title VARCHAR(150),
    comment TEXT,
    is_verified_booking BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_hotel_id (hotel_id),
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating),
    INDEX idx_created_at (created_at),
    UNIQUE KEY unique_booking_review (booking_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 11. STAFF TABLE
-- ============================================
CREATE TABLE staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hotel_id INT,
    department VARCHAR(50),
    position VARCHAR(50),
    salary DECIMAL(10, 2),
    hire_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_hotel_id (hotel_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 12. ROOM_AVAILABILITY TABLE (For tracking availability)
-- ============================================
CREATE TABLE room_availability (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    available_date DATE NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    INDEX idx_room_id (room_id),
    INDEX idx_available_date (available_date),
    UNIQUE KEY unique_room_date (room_id, available_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 13. AUDIT_LOG TABLE (For tracking changes)
-- ============================================
CREATE TABLE audit_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INT,
    action VARCHAR(50) NOT NULL,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_entity_type (entity_type),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 14. CANCELLATION_POLICY TABLE
-- ============================================
CREATE TABLE cancellation_policy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    policy_name VARCHAR(100),
    days_before_checkout INT,
    refund_percentage INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
    INDEX idx_hotel_id (hotel_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 15. NOTIFICATIONS TABLE
-- ============================================
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    booking_id INT,
    notification_type VARCHAR(50),
    title VARCHAR(150),
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SAMPLE DATA (Ready to use!)
-- ============================================

-- Insert sample users
INSERT INTO users (email, password_hash, first_name, last_name, phone_number, user_role, created_at) VALUES
('admin@hotel.com', '$2a$10$slYQmyNdGzirKwstP7RWKuS11LX.cI6xG6Hxr2c/Mly6qOmM8w3MS', 'Admin', 'User', '9876543210', 'ADMIN', NOW()),
('john@example.com', '$2a$10$slYQmyNdGzirKwstP7RWKuS11LX.cI6xG6Hxr2c/Mly6qOmM8w3MS', 'John', 'Doe', '9876543211', 'GUEST', NOW()),
('jane@example.com', '$2a$10$slYQmyNdGzirKwstP7RWKuS11LX.cI6xG6Hxr2c/Mly6qOmM8w3MS', 'Jane', 'Smith', '9876543212', 'GUEST', NOW()),
('mike@example.com', '$2a$10$slYQmyNdGzirKwstP7RWKuS11LX.cI6xG6Hxr2c/Mly6qOmM8w3MS', 'Mike', 'Johnson', '9876543213', 'GUEST', NOW()),
('staff@hotel.com', '$2a$10$slYQmyNdGzirKwstP7RWKuS11LX.cI6xG6Hxr2c/Mly6qOmM8w3MS', 'Staff', 'Member', '9876543214', 'STAFF', NOW());

-- Insert sample hotels
INSERT INTO hotels (name, city, address, phone_number, email, description, image_url, rating, total_reviews, created_at) VALUES
('Grand Palace Hotel', 'New York', '123 Main Street, Manhattan, NY 10001', '212-555-1234', 'info@grandpalace.com', 
 'Luxury 5-star hotel in the heart of Manhattan with world-class amenities, fine dining, and stunning skyline views.', 
 'https://via.placeholder.com/400x300?text=Grand+Palace', 4.5, 120, NOW()),
 
('Ocean View Resort', 'Miami', '456 Beach Avenue, Miami, FL 33139', '305-555-5678', 'info@oceanview.com',
 'Beachfront resort with private beach access, water sports, and tropical ambiance. Perfect for vacation getaways.',
 'https://via.placeholder.com/400x300?text=Ocean+View', 4.7, 95, NOW()),
 
('Mountain Peak Lodge', 'Denver', '789 Summit Road, Denver, CO 80202', '303-555-9999', 'info@mountainpeak.com',
 'Scenic mountain lodge with hiking trails, fresh air, and cozy fireplaces. Ideal for adventure seekers.',
 'https://via.placeholder.com/400x300?text=Mountain+Peak', 4.3, 78, NOW()),
 
('Desert Oasis Inn', 'Phoenix', '321 Cactus Lane, Phoenix, AZ 85001', '602-555-4321', 'info@desertoasis.com',
 'Modern inn in the desert with poolside bar, spa services, and warm hospitality.',
 'https://via.placeholder.com/400x300?text=Desert+Oasis', 4.2, 65, NOW()),
 
('Lakeside Retreat', 'Seattle', '654 Water Street, Seattle, WA 98101', '206-555-8765', 'info@lakesideretreat.com',
 'Peaceful retreat overlooking beautiful lakes with fishing, boating, and nature activities.',
 'https://via.placeholder.com/400x300?text=Lakeside+Retreat', 4.4, 87, NOW());

-- Insert hotel amenities
INSERT INTO hotel_amenities (hotel_id, amenity_name) VALUES
(1, 'WiFi'), (1, 'Gym'), (1, 'Swimming Pool'), (1, 'Restaurant'), (1, 'Spa'), (1, 'Parking'),
(2, 'WiFi'), (2, 'Gym'), (2, 'Beach'), (2, 'Water Sports'), (2, 'Restaurant'), (2, 'Bar'),
(3, 'WiFi'), (3, 'Hiking Trails'), (3, 'Fireplace'), (3, 'Restaurant'), (3, 'Mountain View'),
(4, 'WiFi'), (4, 'Pool'), (4, 'Spa'), (4, 'Bar'), (4, 'Desert Tours'),
(5, 'WiFi'), (5, 'Lake View'), (5, 'Fishing'), (5, 'Boating'), (5, 'Restaurant');

-- Insert sample rooms
INSERT INTO rooms (hotel_id, room_number, room_type, price_per_night, occupancy, description, is_available, created_at) VALUES
-- Grand Palace Hotel rooms
(1, '101', 'SINGLE', 150.00, 1, 'Cozy single room with city view, perfect for solo travelers', TRUE, NOW()),
(1, '102', 'SINGLE', 150.00, 1, 'Single room with work desk and high-speed internet', TRUE, NOW()),
(1, '201', 'DOUBLE', 220.00, 2, 'Spacious double room with king bed and modern amenities', TRUE, NOW()),
(1, '202', 'DOUBLE', 220.00, 2, 'Double room with sofa, perfect for couples and small families', TRUE, NOW()),
(1, '301', 'SUITE', 400.00, 4, 'Luxurious suite with separate living area and bedroom, mini bar', TRUE, NOW()),
(1, '401', 'DELUXE', 280.00, 2, 'Deluxe room with balcony, premium toiletries, and city views', TRUE, NOW()),

-- Ocean View Resort rooms
(2, '101', 'SINGLE', 140.00, 1, 'Single room with beach view and direct balcony access', TRUE, NOW()),
(2, '102', 'DOUBLE', 200.00, 2, 'Double beachfront room with private pool', TRUE, NOW()),
(2, '201', 'SUITE', 380.00, 4, 'Luxury beach suite with jacuzzi and ocean view', TRUE, NOW()),
(2, '301', 'DELUXE', 260.00, 2, 'Deluxe beachfront room with complimentary water sports', TRUE, NOW()),
(2, '302', 'DELUXE', 260.00, 2, 'Deluxe room with sunset view and beach access', TRUE, NOW()),

-- Mountain Peak Lodge rooms
(3, '101', 'SINGLE', 120.00, 1, 'Cozy single with mountain view and fireplace', TRUE, NOW()),
(3, '102', 'DOUBLE', 190.00, 2, 'Double room with hiking trail view', TRUE, NOW()),
(3, '201', 'SUITE', 350.00, 4, 'Mountain suite with fireplace and panoramic views', TRUE, NOW()),
(3, '301', 'DELUXE', 240.00, 2, 'Deluxe mountain room with hot tub on balcony', TRUE, NOW()),

-- Desert Oasis Inn rooms
(4, '101', 'SINGLE', 110.00, 1, 'Single room with pool view', TRUE, NOW()),
(4, '102', 'DOUBLE', 180.00, 2, 'Double room with garden view', TRUE, NOW()),
(4, '201', 'SUITE', 320.00, 4, 'Luxury suite with private spa', TRUE, NOW()),
(4, '301', 'DELUXE', 230.00, 2, 'Deluxe room with desert view and spa access', TRUE, NOW()),

-- Lakeside Retreat rooms
(5, '101', 'SINGLE', 130.00, 1, 'Single room with lake view and fishing spot', TRUE, NOW()),
(5, '102', 'DOUBLE', 210.00, 2, 'Double room with private dock and boating access', TRUE, NOW()),
(5, '201', 'SUITE', 360.00, 4, 'Lakeside suite with outdoor hot tub', TRUE, NOW()),
(5, '301', 'DELUXE', 250.00, 2, 'Deluxe room with sunrise lake view', TRUE, NOW());

-- Insert room amenities
INSERT INTO room_amenities (room_id, amenity_name) VALUES
(1, 'AC'), (1, 'TV'), (1, 'WiFi'), (1, 'Work Desk'),
(2, 'AC'), (2, 'TV'), (2, 'WiFi'), (2, 'Work Desk'), (2, 'High Speed Internet'),
(3, 'AC'), (3, 'TV'), (3, 'WiFi'), (3, 'King Bed'), (3, 'Sofa'),
(4, 'AC'), (4, 'TV'), (4, 'WiFi'), (4, 'King Bed'), (4, 'Sofa'),
(5, 'AC'), (5, 'TV'), (5, 'WiFi'), (5, 'Kitchen'), (5, 'Living Area'), (5, 'Balcony'), (5, 'Mini Bar'),
(6, 'AC'), (6, 'TV'), (6, 'WiFi'), (6, 'Balcony'), (6, 'Premium Toiletries'),
(7, 'AC'), (7, 'TV'), (7, 'WiFi'), (7, 'Beach View'),
(8, 'AC'), (8, 'TV'), (8, 'WiFi'), (8, 'Beach Access'), (8, 'Private Pool'),
(9, 'AC'), (9, 'TV'), (9, 'WiFi'), (9, 'Kitchen'), (9, 'Jacuzzi'), (9, 'Ocean View'),
(10, 'AC'), (10, 'TV'), (10, 'WiFi'), (10, 'Beach Access'), (10, 'Water Sports'),
(11, 'AC'), (11, 'TV'), (11, 'WiFi'), (11, 'Sunset View'), (11, 'Beach Access'),
(12, 'AC'), (12, 'TV'), (12, 'WiFi'), (12, 'Fireplace'), (12, 'Mountain View'),
(13, 'AC'), (13, 'TV'), (13, 'WiFi'), (13, 'Hiking Trail View'),
(14, 'AC'), (14, 'TV'), (14, 'WiFi'), (14, 'Fireplace'), (14, 'Panoramic View'),
(15, 'AC'), (15, 'TV'), (15, 'WiFi'), (15, 'Hot Tub'), (15, 'Balcony'),
(16, 'AC'), (16, 'TV'), (16, 'WiFi'), (16, 'Pool View'),
(17, 'AC'), (17, 'TV'), (17, 'WiFi'), (17, 'Garden View'),
(18, 'AC'), (18, 'TV'), (18, 'WiFi'), (18, 'Private Spa'),
(19, 'AC'), (19, 'TV'), (19, 'WiFi'), (19, 'Desert View'), (19, 'Spa Access'),
(20, 'AC'), (20, 'TV'), (20, 'WiFi'), (20, 'Lake View'), (20, 'Fishing'),
(21, 'AC'), (21, 'TV'), (21, 'WiFi'), (21, 'Private Dock'), (21, 'Boating Access'),
(22, 'AC'), (22, 'TV'), (22, 'WiFi'), (22, 'Outdoor Hot Tub'), (22, 'Lake View'),
(23, 'AC'), (23, 'TV'), (23, 'WiFi'), (23, 'Sunrise View');

-- Insert room images
INSERT INTO room_images (room_id, image_url, is_primary) VALUES
(1, 'https://via.placeholder.com/600x400?text=Room+101', TRUE),
(2, 'https://via.placeholder.com/600x400?text=Room+102', TRUE),
(3, 'https://via.placeholder.com/600x400?text=Room+201', TRUE),
(4, 'https://via.placeholder.com/600x400?text=Room+202', TRUE),
(5, 'https://via.placeholder.com/600x400?text=Suite+301', TRUE),
(6, 'https://via.placeholder.com/600x400?text=Deluxe+401', TRUE),
(7, 'https://via.placeholder.com/600x400?text=Beach+Single', TRUE),
(8, 'https://via.placeholder.com/600x400?text=Beach+Double', TRUE),
(9, 'https://via.placeholder.com/600x400?text=Beach+Suite', TRUE),
(10, 'https://via.placeholder.com/600x400?text=Beach+Deluxe', TRUE),
(11, 'https://via.placeholder.com/600x400?text=Sunset+Deluxe', TRUE);

-- Insert sample bookings
INSERT INTO bookings (booking_number, user_id, hotel_id, room_id, check_in_date, check_out_date, number_of_guests, total_price, status, special_requests, created_at) VALUES
('BK-1001-20240115', 2, 1, 3, '2024-02-01', '2024-02-05', 2, 880.00, 'CONFIRMED', 'Early check-in if possible', NOW()),
('BK-1002-20240115', 3, 2, 8, '2024-02-10', '2024-02-15', 2, 1000.00, 'CONFIRMED', 'Beach view preferred', NOW()),
('BK-1003-20240115', 4, 3, 13, '2024-02-20', '2024-02-23', 2, 570.00, 'PENDING', 'Hiking tour arrangement', NOW()),
('BK-1004-20240115', 2, 1, 4, '2024-03-01', '2024-03-03', 2, 440.00, 'COMPLETED', 'None', NOW()),
('BK-1005-20240115', 3, 2, 10, '2024-03-05', '2024-03-08', 2, 780.00, 'CANCELLED', 'Personal reasons', NOW());

-- Insert sample payments
INSERT INTO payments (booking_id, amount, payment_method, transaction_id, status, payment_gateway, created_at) VALUES
(1, 880.00, 'CREDIT_CARD', 'TXN-2024-001ABC', 'SUCCESS', 'RAZORPAY', NOW()),
(2, 1000.00, 'DEBIT_CARD', 'TXN-2024-002DEF', 'SUCCESS', 'STRIPE', NOW()),
(3, 570.00, 'NET_BANKING', 'TXN-2024-003GHI', 'PENDING', 'RAZORPAY', NOW()),
(4, 440.00, 'CREDIT_CARD', 'TXN-2024-004JKL', 'SUCCESS', 'STRIPE', NOW());

-- Insert sample reviews
INSERT INTO reviews (booking_id, hotel_id, user_id, rating, title, comment, is_verified_booking, created_at) VALUES
(4, 1, 2, 5, 'Amazing Experience!', 'Excellent hotel with great service. Staff was very friendly and rooms were clean and comfortable. Will definitely come back!', TRUE, NOW()),
(2, 2, 3, 4, 'Great Beach Resort', 'Beautiful beachfront property. The water sports activities were fun. Room was nice but a bit small.', TRUE, NOW()),
(1, 1, 2, 4.5, 'Wonderful Stay', 'Great location, comfortable beds, and the restaurant food was delicious. Highly recommended!', TRUE, NOW());

-- Insert staff information
INSERT INTO staff (user_id, hotel_id, department, position, salary, hire_date, is_active) VALUES
(5, 1, 'Front Desk', 'Manager', 45000.00, '2023-01-15', TRUE),
(5, 2, 'Housekeeping', 'Supervisor', 35000.00, '2023-03-20', TRUE);

-- Insert cancellation policies
INSERT INTO cancellation_policy (hotel_id, policy_name, days_before_checkout, refund_percentage) VALUES
(1, 'Flexible', 7, 100),
(1, 'Moderate', 3, 50),
(1, 'Non-Refundable', 0, 0),
(2, 'Flexible', 7, 100),
(2, 'Moderate', 3, 50),
(3, 'Standard', 5, 75),
(4, 'Flexible', 7, 100),
(5, 'Moderate', 3, 50);

-- ============================================
-- VIEWS (Optional - for common queries)
-- ============================================

-- View: Available Rooms
CREATE VIEW available_rooms AS
SELECT 
    r.id,
    r.room_number,
    r.room_type,
    r.price_per_night,
    r.occupancy,
    h.id as hotel_id,
    h.name as hotel_name,
    h.city,
    h.rating
FROM rooms r
JOIN hotels h ON r.hotel_id = h.id
WHERE r.is_available = TRUE AND r.deleted_at IS NULL;

-- View: Recent Bookings
CREATE VIEW recent_bookings AS
SELECT 
    b.id,
    b.booking_number,
    b.user_id,
    u.first_name,
    u.last_name,
    h.name as hotel_name,
    r.room_number,
    b.check_in_date,
    b.check_out_date,
    b.total_price,
    b.status
FROM bookings b
JOIN users u ON b.user_id = u.id
JOIN hotels h ON b.hotel_id = h.id
JOIN rooms r ON b.room_id = r.id
WHERE b.created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)
ORDER BY b.created_at DESC;

-- View: Hotel Revenue
CREATE VIEW hotel_revenue AS
SELECT 
    h.id,
    h.name,
    COUNT(DISTINCT b.id) as total_bookings,
    SUM(b.total_price) as total_revenue,
    AVG(b.total_price) as avg_booking_value,
    h.rating,
    h.total_reviews
FROM hotels h
LEFT JOIN bookings b ON h.id = b.hotel_id AND b.status IN ('CONFIRMED', 'COMPLETED')
GROUP BY h.id, h.name, h.rating, h.total_reviews;

-- ============================================
-- STORED PROCEDURES (Optional)
-- ============================================

DELIMITER $$

-- Procedure: Create Booking
CREATE PROCEDURE create_booking(
    IN p_user_id INT,
    IN p_hotel_id INT,
    IN p_room_id INT,
    IN p_check_in DATE,
    IN p_check_out DATE,
    IN p_num_guests INT,
    IN p_special_requests TEXT,
    OUT p_booking_id INT
)
BEGIN
    DECLARE v_nights INT;
    DECLARE v_price DECIMAL(10, 2);
    DECLARE v_total_price DECIMAL(10, 2);
    DECLARE v_booking_number VARCHAR(50);
    
    -- Calculate number of nights
    SET v_nights = DATEDIFF(p_check_out, p_check_in);
    
    -- Get room price
    SELECT price_per_night INTO v_price FROM rooms WHERE id = p_room_id;
    
    -- Calculate total price
    SET v_total_price = v_price * v_nights;
    
    -- Generate booking number
    SET v_booking_number = CONCAT('BK-', FLOOR(RAND()*100000), '-', DATE_FORMAT(NOW(), '%Y%m%d'));
    
    -- Insert booking
    INSERT INTO bookings (booking_number, user_id, hotel_id, room_id, check_in_date, check_out_date, 
                         number_of_guests, total_price, status, special_requests)
    VALUES (v_booking_number, p_user_id, p_hotel_id, p_room_id, p_check_in, p_check_out, 
            p_num_guests, v_total_price, 'PENDING', p_special_requests);
    
    SET p_booking_id = LAST_INSERT_ID();
END$$

-- Procedure: Cancel Booking
CREATE PROCEDURE cancel_booking(
    IN p_booking_id INT,
    IN p_cancellation_reason VARCHAR(255)
)
BEGIN
    UPDATE bookings 
    SET status = 'CANCELLED', 
        cancelled_at = NOW(),
        updated_at = NOW()
    WHERE id = p_booking_id;
END$$

DELIMITER ;
