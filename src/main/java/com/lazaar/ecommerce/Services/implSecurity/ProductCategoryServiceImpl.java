package com.lazaar.ecommerce.Services.implSecurity;

import com.lazaar.ecommerce.Repositories.ProductCategoryRepository;
import com.lazaar.ecommerce.models.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryRepository.findAll();
    }

    public ProductCategory createCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    // Update an existing product category
    public ProductCategory updateCategory(Long id, ProductCategory productCategory) {
        Optional<ProductCategory> existingCategory = productCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            ProductCategory categoryToUpdate = existingCategory.get();
            categoryToUpdate.setCategoryName(productCategory.getCategoryName()); // Update category name
            // Update other fields as required
            return productCategoryRepository.save(categoryToUpdate);
        }
        throw new RuntimeException("Category not found with id: " + id);
    }

    // Delete a product category
    public void deleteCategory(Long id) {
        Optional<ProductCategory> existingCategory = productCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            productCategoryRepository.delete(existingCategory.get());
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }
}
