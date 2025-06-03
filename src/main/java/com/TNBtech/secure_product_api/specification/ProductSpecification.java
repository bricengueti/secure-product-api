package com.TNBtech.secure_product_api.specification;

import com.TNBtech.secure_product_api.entities.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> getProductSpecification(
            String name,
            String description,
            Double minPrice,
            Double maxPrice,
            Long categoryId,
            String categoryName
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (description != null && !description.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }

            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (categoryId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
            }

            if (categoryName != null && !categoryName.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("category").get("name")), "%" + categoryName.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
