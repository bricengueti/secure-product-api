package com.TNBtech.secure_product_api.services;

import com.TNBtech.secure_product_api.DTO.ProductRequestDto;
import com.TNBtech.secure_product_api.DTO.ProductResponseDto;
import com.TNBtech.secure_product_api.entities.Category;
import com.TNBtech.secure_product_api.entities.Product;
import com.TNBtech.secure_product_api.mapper.ProductMapper;
import com.TNBtech.secure_product_api.repositories.CategoryRepository;
import com.TNBtech.secure_product_api.repositories.ProductRepository;
import com.TNBtech.secure_product_api.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper();
    }

    public ProductResponseDto createProduct(ProductRequestDto request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return productMapper.apply(saved);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return productMapper.apply(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit introuvable pour suppression");
        }
        productRepository.deleteById(id);
    }

    public Page<ProductResponseDto> searchProducts(
            Pageable pageable,
            String name,
            String description,
            Double minPrice,
            Double maxPrice,
            Long categoryId,
            String categoryName
    ) {
        Specification<Product> spec = ProductSpecification.getProductSpecification(
                name, description, minPrice, maxPrice, categoryId, categoryName
        );

        return productRepository.findAll(spec, pageable)
                .map(productMapper::apply);
    }
}
