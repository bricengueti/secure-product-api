package com.TNBtech.secure_product_api.services;

import com.TNBtech.secure_product_api.DTO.CategoryReponseDto;
import com.TNBtech.secure_product_api.DTO.CategoryRequestDto;
import com.TNBtech.secure_product_api.entities.Category;
import com.TNBtech.secure_product_api.mapper.CategoryMapper;
import com.TNBtech.secure_product_api.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = new CategoryMapper();
    }
    public List<CategoryReponseDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::apply)
                .collect(Collectors.toList());
    }
    public CategoryReponseDto categoryToDto(Category category) {
        return categoryMapper.apply(category);
    }

    public CategoryReponseDto create(CategoryRequestDto request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());

        Category saved = categoryRepository.save(category);
        return categoryToDto(saved);
    }

    public CategoryReponseDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID : " + id));
        return categoryToDto(category);
    }

    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Catégorie introuvable avec l'ID : " + id);
        }
        categoryRepository.deleteById(id);
    }
}
