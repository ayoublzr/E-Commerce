package com.lazaar.ecommerce.Repositories;

import com.lazaar.ecommerce.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
