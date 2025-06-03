package com.TNBtech.secure_product_api.DTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "Le nom est obligatoire")
        String name,

        String description
) {
}
