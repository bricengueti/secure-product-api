package com.TNBtech.secure_product_api.DTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryReponseDto(

        Long id,

        String name,

        String description
) {
}
