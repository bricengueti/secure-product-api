package com.TNBtech.secure_product_api.controller;

import com.TNBtech.secure_product_api.DTO.CategoryRequestDto;
import com.TNBtech.secure_product_api.DTO.CategoryReponseDto;
import com.TNBtech.secure_product_api.services.CategoryService;
import com.TNBtech.secure_product_api.utils.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Gestion des catégories de produits")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Lister toutes les catégories")
    public ResponseEntity<ApiResponses<List<CategoryReponseDto>>> getAll() {
        List<CategoryReponseDto> list = categoryService.getAll();
        return ResponseEntity.ok(new ApiResponses<>("Liste des catégories récupérée avec succès", list, HttpStatus.OK.value()));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle catégorie")
    public ResponseEntity<ApiResponses<CategoryReponseDto>> create(@RequestBody CategoryRequestDto request) {
        CategoryReponseDto created = categoryService.create(request);
        return new ResponseEntity<>(
                new ApiResponses<>("Catégorie créée avec succès", created, HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par ID")
    public ResponseEntity<ApiResponses<CategoryReponseDto>> getById(@PathVariable Long id) {
        CategoryReponseDto dto = categoryService.getById(id);
        return ResponseEntity.ok(new ApiResponses<>("Catégorie récupérée avec succès", dto, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie par ID")
    public ResponseEntity<ApiResponses<Void>> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok(new ApiResponses<>("Catégorie supprimée avec succès", null, HttpStatus.OK.value()));
    }
}
