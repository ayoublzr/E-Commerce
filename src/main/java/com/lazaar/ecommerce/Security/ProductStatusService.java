package com.lazaar.ecommerce.Security;

import com.lazaar.ecommerce.models.Product;
import com.lazaar.ecommerce.models.ProductStatus;

import java.util.List;

public interface ProductStatusService {
    List<ProductStatus> getProductStatusList();
    ProductStatus saveProductStatus(ProductStatus productStatus);
}
