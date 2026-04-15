# Hotel Management System - API Contract

**Base URL:** `http://localhost:8080/api/v1`

---

## 1. Authentication APIs

### 1.1 User Register
- **Endpoint:** `POST /auth/register`
- **Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "9876543210",
  "userRole": "GUEST"
}
```
- **Response:** `201 Created`
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "userRole": "GUEST",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### 1.2 User Login
- **Endpoint:** `POST /auth/login`
- **Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123"
}
```
- **Response:** `200 OK`
```json
{
  "token": "jwt_token_here",
  "userId": 1,
  "firstName": "John",
  "userRole": "GUEST"
}
```

### 1.3 User Logout
- **Endpoint:** `POST /auth/logout`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** `200 OK`
```json
{
  "message": "Logged out successfully"
}
```

---

## 2. Hotel APIs

### 2.1 Get All Hotels
- **Endpoint:** `GET /hotels`
- **Query Parameters:**
  - `page` (optional): default 0
  - `size` (optional): default 10
  - `city` (optional): filter by city
  - `minPrice` (optional): filter by minimum price
  - `maxPrice` (optional): filter by maximum price
- **Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "name": "Grand Palace Hotel",
      "city": "New York",
      "address": "123 Main Street, NY 10001",
      "phoneNumber": "2125551234",
      "email": "info@grandpalace.com",
      "rating": 4.5,
      "totalReviews": 120,
      "image": "url_to_image",
      "amenities": ["WiFi", "Gym", "Swimming Pool", "Restaurant"],
      "description": "Luxury 5-star hotel with world-class amenities"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

### 2.2 Get Hotel by ID
- **Endpoint:** `GET /hotels/{hotelId}`
- **Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Grand Palace Hotel",
  "city": "New York",
  "address": "123 Main Street, NY 10001",
  "phoneNumber": "2125551234",
  "email": "info@grandpalace.com",
  "rating": 4.5,
  "totalReviews": 120,
  "image": "url_to_image",
  "amenities": ["WiFi", "Gym", "Swimming Pool", "Restaurant"],
  "description": "Luxury 5-star hotel with world-class amenities",
  "rooms": [
    {
      "id": 101,
      "roomNumber": "101",
      "roomType": "DELUXE",
      "pricePerNight": 250,
      "occupancy": 2,
      "amenities": ["AC", "TV", "WiFi"],
      "isAvailable": true,
      "images": ["url1", "url2"]
    }
  ]
}
```

### 2.3 Create Hotel (Admin)
- **Endpoint:** `POST /hotels`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Request Body:**
```json
{
  "name": "Grand Palace Hotel",
  "city": "New York",
  "address": "123 Main Street, NY 10001",
  "phoneNumber": "2125551234",
  "email": "info@grandpalace.com",
  "image": "url_to_image",
  "amenities": ["WiFi", "Gym", "Swimming Pool", "Restaurant"],
  "description": "Luxury 5-star hotel with world-class amenities"
}
```
- **Response:** `201 Created`

### 2.4 Update Hotel (Admin)
- **Endpoint:** `PUT /hotels/{hotelId}`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Request Body:** Same as Create Hotel
- **Response:** `200 OK`

### 2.5 Delete Hotel (Admin)
- **Endpoint:** `DELETE /hotels/{hotelId}`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Response:** `204 No Content`

---

## 3. Room APIs

### 3.1 Get Rooms by Hotel
- **Endpoint:** `GET /hotels/{hotelId}/rooms`
- **Query Parameters:**
  - `roomType` (optional): SINGLE, DOUBLE, SUITE, DELUXE
  - `priceMin` (optional)
  - `priceMax` (optional)
  - `occupancy` (optional)
- **Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 101,
      "roomNumber": "101",
      "roomType": "DELUXE",
      "pricePerNight": 250,
      "occupancy": 2,
      "amenities": ["AC", "TV", "WiFi", "Balcony"],
      "isAvailable": true,
      "images": ["url1", "url2"],
      "description": "Spacious deluxe room with city view"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

### 3.2 Get Room by ID
- **Endpoint:** `GET /hotels/{hotelId}/rooms/{roomId}`
- **Response:** `200 OK` (Same as room object above)

### 3.3 Create Room (Admin)
- **Endpoint:** `POST /hotels/{hotelId}/rooms`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Request Body:**
```json
{
  "roomNumber": "101",
  "roomType": "DELUXE",
  "pricePerNight": 250,
  "occupancy": 2,
  "amenities": ["AC", "TV", "WiFi", "Balcony"],
  "images": ["url1", "url2"],
  "description": "Spacious deluxe room with city view"
}
```
- **Response:** `201 Created`

### 3.4 Update Room (Admin)
- **Endpoint:** `PUT /hotels/{hotelId}/rooms/{roomId}`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Request Body:** Same as Create Room
- **Response:** `200 OK`

### 3.5 Delete Room (Admin)
- **Endpoint:** `DELETE /hotels/{hotelId}/rooms/{roomId}`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Response:** `204 No Content`

### 3.6 Check Room Availability
- **Endpoint:** `GET /hotels/{hotelId}/rooms/{roomId}/availability`
- **Query Parameters:**
  - `checkInDate` (required): YYYY-MM-DD
  - `checkOutDate` (required): YYYY-MM-DD
- **Response:** `200 OK`
```json
{
  "roomId": 101,
  "isAvailable": true,
  "checkInDate": "2024-02-01",
  "checkOutDate": "2024-02-05"
}
```

---

## 4. Booking APIs

### 4.1 Create Booking
- **Endpoint:** `POST /bookings`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "hotelId": 1,
  "roomId": 101,
  "checkInDate": "2024-02-01",
  "checkOutDate": "2024-02-05",
  "numberOfGuests": 2,
  "specialRequests": "Late check-in requested"
}
```
- **Response:** `201 Created`
```json
{
  "id": 1001,
  "bookingNumber": "BK-1001-20240115",
  "userId": 1,
  "hotelId": 1,
  "roomId": 101,
  "checkInDate": "2024-02-01",
  "checkOutDate": "2024-02-05",
  "numberOfGuests": 2,
  "totalPrice": 1000,
  "status": "CONFIRMED",
  "paymentStatus": "PENDING",
  "specialRequests": "Late check-in requested",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### 4.2 Get My Bookings
- **Endpoint:** `GET /bookings/my-bookings`
- **Headers:** `Authorization: Bearer {token}`
- **Query Parameters:**
  - `status` (optional): ALL, CONFIRMED, CANCELLED, COMPLETED
  - `page` (optional): default 0
- **Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1001,
      "bookingNumber": "BK-1001-20240115",
      "hotelName": "Grand Palace Hotel",
      "roomNumber": "101",
      "checkInDate": "2024-02-01",
      "checkOutDate": "2024-02-05",
      "totalPrice": 1000,
      "status": "CONFIRMED",
      "paymentStatus": "PENDING"
    }
  ],
  "totalElements": 15,
  "totalPages": 2,
  "currentPage": 0
}
```

### 4.3 Get Booking by ID
- **Endpoint:** `GET /bookings/{bookingId}`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** `200 OK` (Same as create booking response)

### 4.4 Update Booking
- **Endpoint:** `PUT /bookings/{bookingId}`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "checkInDate": "2024-02-02",
  "checkOutDate": "2024-02-06",
  "numberOfGuests": 3,
  "specialRequests": "Updated request"
}
```
- **Response:** `200 OK`

### 4.5 Cancel Booking
- **Endpoint:** `POST /bookings/{bookingId}/cancel`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "cancellationReason": "Change of plans"
}
```
- **Response:** `200 OK`
```json
{
  "id": 1001,
  "status": "CANCELLED",
  "refundAmount": 800,
  "cancellationDate": "2024-01-15T11:00:00Z"
}
```

### 4.6 Get All Bookings (Admin)
- **Endpoint:** `GET /bookings`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Query Parameters:**
  - `hotelId` (optional)
  - `status` (optional)
  - `page` (optional)
- **Response:** `200 OK` (Paginated list of bookings)

---

## 5. Payment APIs

### 5.1 Create Payment
- **Endpoint:** `POST /payments`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "bookingId": 1001,
  "amount": 1000,
  "paymentMethod": "CREDIT_CARD",
  "cardDetails": {
    "cardNumber": "4111111111111111",
    "expiryMonth": "12",
    "expiryYear": "2025",
    "cvv": "123"
  }
}
```
- **Response:** `201 Created`
```json
{
  "id": 5001,
  "bookingId": 1001,
  "amount": 1000,
  "paymentMethod": "CREDIT_CARD",
  "transactionId": "TXN-5001-ABC123",
  "status": "SUCCESS",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### 5.2 Get Payment by ID
- **Endpoint:** `GET /payments/{paymentId}`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** `200 OK`

### 5.3 Refund Payment
- **Endpoint:** `POST /payments/{paymentId}/refund`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Request Body:**
```json
{
  "reason": "Booking cancelled"
}
```
- **Response:** `200 OK`
```json
{
  "id": 5001,
  "refundStatus": "SUCCESS",
  "refundAmount": 1000,
  "refundTransactionId": "REF-5001-XYZ789"
}
```

---

## 6. Review APIs

### 6.1 Create Review
- **Endpoint:** `POST /reviews`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "bookingId": 1001,
  "hotelId": 1,
  "rating": 4.5,
  "title": "Excellent stay",
  "comment": "Great hotel with excellent service"
}
```
- **Response:** `201 Created`
```json
{
  "id": 2001,
  "bookingId": 1001,
  "hotelId": 1,
  "userId": 1,
  "userName": "John Doe",
  "rating": 4.5,
  "title": "Excellent stay",
  "comment": "Great hotel with excellent service",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### 6.2 Get Reviews by Hotel
- **Endpoint:** `GET /hotels/{hotelId}/reviews`
- **Query Parameters:**
  - `page` (optional): default 0
  - `sortBy` (optional): RECENT, RATING_HIGH, RATING_LOW
- **Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 2001,
      "userName": "John Doe",
      "rating": 4.5,
      "title": "Excellent stay",
      "comment": "Great hotel with excellent service",
      "createdAt": "2024-01-10T10:30:00Z"
    }
  ],
  "totalElements": 45,
  "totalPages": 5,
  "currentPage": 0,
  "averageRating": 4.3
}
```

### 6.3 Update Review
- **Endpoint:** `PUT /reviews/{reviewId}`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:** Same as Create Review
- **Response:** `200 OK`

### 6.4 Delete Review
- **Endpoint:** `DELETE /reviews/{reviewId}`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** `204 No Content`

---

## 7. User Profile APIs

### 7.1 Get User Profile
- **Endpoint:** `GET /users/profile`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** `200 OK`
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "9876543210",
  "userRole": "GUEST",
  "dateOfBirth": "1990-05-15",
  "address": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "country": "USA",
  "zipCode": "10001",
  "createdAt": "2024-01-01T10:30:00Z"
}
```

### 7.2 Update User Profile
- **Endpoint:** `PUT /users/profile`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "9876543210",
  "dateOfBirth": "1990-05-15",
  "address": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "country": "USA",
  "zipCode": "10001"
}
```
- **Response:** `200 OK`

### 7.3 Change Password
- **Endpoint:** `POST /users/change-password`
- **Headers:** `Authorization: Bearer {token}`
- **Request Body:**
```json
{
  "oldPassword": "CurrentPassword123",
  "newPassword": "NewPassword123"
}
```
- **Response:** `200 OK`
```json
{
  "message": "Password changed successfully"
}
```

---

## 8. Admin Dashboard APIs

### 8.1 Get Dashboard Stats
- **Endpoint:** `GET /admin/dashboard/stats`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Response:** `200 OK`
```json
{
  "totalHotels": 50,
  "totalRooms": 2500,
  "totalBookings": 15000,
  "totalRevenue": 2500000,
  "occupancyRate": 75.5,
  "pendingPayments": 50000,
  "todayBookings": 45,
  "averageRating": 4.2
}
```

### 8.2 Get Revenue Report
- **Endpoint:** `GET /admin/reports/revenue`
- **Headers:** `Authorization: Bearer {token}`, `Role: ADMIN`
- **Query Parameters:**
  - `startDate` (optional): YYYY-MM-DD
  - `endDate` (optional): YYYY-MM-DD
  - `hotelId` (optional)
- **Response:** `200 OK`
```json
{
  "period": {
    "startDate": "2024-01-01",
    "endDate": "2024-01-31"
  },
  "totalRevenue": 250000,
  "totalBookings": 500,
  "averageBookingValue": 500,
  "topHotels": [
    {
      "hotelId": 1,
      "hotelName": "Grand Palace",
      "revenue": 50000
    }
  ]
}
```

---

## Error Response Format

All error responses follow this format:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message",
  "path": "/api/v1/hotels"
}
```

### Common HTTP Status Codes
- `200 OK`: Successful GET/PUT
- `201 Created`: Successful POST
- `204 No Content`: Successful DELETE
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Business logic conflict
- `500 Internal Server Error`: Server error

---

## Authentication Headers

All protected endpoints require:
```
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

---

## Pagination

Paginated responses include:
- `content`: Array of items
- `totalElements`: Total count of items
- `totalPages`: Total number of pages
- `currentPage`: Current page number (0-indexed)
- `pageSize`: Number of items per page
