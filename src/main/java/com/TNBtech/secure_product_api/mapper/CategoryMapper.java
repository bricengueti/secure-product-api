package com.TNBtech.secure_product_api.mapper;

import com.TNBtech.secure_product_api.DTO.CategoryReponseDto;
import com.TNBtech.secure_product_api.entities.Category;

import java.util.function.Function;

public class CategoryMapper implements Function<Category, CategoryReponseDto> {

    @Override
    public CategoryReponseDto apply(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryReponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
