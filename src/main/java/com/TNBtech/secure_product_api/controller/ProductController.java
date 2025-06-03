package com.TNBtech.secure_product_api.controller;

import com.TNBtech.secure_product_api.DTO.ProductRequestDto;
import com.TNBtech.secure_product_api.DTO.ProductResponseDto;
import com.TNBtech.secure_product_api.services.ProductService;
import com.TNBtech.secure_product_api.utils.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Création de produit
    @PostMapping
    public ResponseEntity<ApiResponses<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto request) {
        ProductResponseDto response = productService.createProduct(request);
        return ResponseEntity.ok(new ApiResponses<>("Produit créé avec succès", response, 200));
    }

    // Récupération par ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponses<ProductResponseDto>> getProductById(@PathVariable Long id) {
        ProductResponseDto response = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponses<>("Produit trouvé", response, 200));
    }

    // Suppression
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponses<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponses<>("Produit supprimé", "Deleted ID: " + id, 200));
    }

    // Recherche paginée avec critères
    @GetMapping
    public ResponseEntity<ApiResponses<Page<ProductResponseDto>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName,
            Pageable pageable
    ) {
        Page<ProductResponseDto> response = productService.searchProducts(
                pageable, name, description, minPrice, maxPrice, categoryId, categoryName
        );
        return ResponseEntity.ok(new ApiResponses<>("Produits récupérés avec pagination", response, 200));
    }
}
