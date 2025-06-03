package com.TNBtech.secure_product_api.DTO;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}
