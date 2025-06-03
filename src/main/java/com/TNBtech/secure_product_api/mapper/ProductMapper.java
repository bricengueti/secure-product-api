package com.TNBtech.secure_product_api.mapper;

import com.TNBtech.secure_product_api.DTO.CategoryReponseDto;
import com.TNBtech.secure_product_api.DTO.ProductResponseDto;
import com.TNBtech.secure_product_api.entities.Product;

import java.util.function.Function;

public class ProductMapper implements Function<Product, ProductResponseDto> {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Override
    public ProductResponseDto apply(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                categoryMapper.apply(product.getCategory())
        );
    }
}
