package com.TNBtech.secure_product_api.DTO;

public record ProductRequestDto(
        String name,
        String description,
        Double price,
        Long categoryId
) {
}

