package com.lazaar.ecommerce.Repositories;

import com.lazaar.ecommerce.models.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
}
