package com.lazaar.ecommerce.Controllers;

import com.lazaar.ecommerce.Security.ProductStatusService;
import com.lazaar.ecommerce.models.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductStatusController {

    @Autowired
    private ProductStatusService productStatusService;

    @GetMapping("/productstatus")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ProductStatus> getProductStatusList() {
        return productStatusService.getProductStatusList();
    }

    // Endpoint to create a new ProductStatus
    @PostMapping("/productstatus")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductStatus> createProductStatus(
            @RequestBody ProductStatus productStatus) {
        try {
            ProductStatus createdStatus = productStatusService.saveProductStatus(productStatus);
            return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
