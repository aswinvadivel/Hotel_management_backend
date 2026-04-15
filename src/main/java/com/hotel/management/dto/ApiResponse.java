package com.hotel.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Integer status;

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).status(200).build();
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).status(201).build();
    }

    public static <T> ApiResponse<T> error(String message, Integer status) {
        return ApiResponse.<T>builder().success(false).message(message).status(status).build();
    }
}
