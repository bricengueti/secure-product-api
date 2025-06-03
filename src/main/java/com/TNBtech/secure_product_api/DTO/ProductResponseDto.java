package com.TNBtech.secure_product_api.DTO;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        Double price,
        CategoryReponseDto category
) {
}
