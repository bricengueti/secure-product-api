package com.TNBtech.secure_product_api.DTO;

public record RegisterRequestDto(
        String fullName,
        String username,
        String email,
        String password
) {
}
