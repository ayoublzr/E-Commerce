package com.lazaar.ecommerce.Controllers;

import com.lazaar.ecommerce.DTO.request.ProductDto;
import com.lazaar.ecommerce.Repositories.ProductCategoryRepository;
import com.lazaar.ecommerce.Repositories.ProductStatusRepository;
import com.lazaar.ecommerce.Security.ProductsService;
import com.lazaar.ecommerce.models.Product;
import com.lazaar.ecommerce.models.ProductCategory;
import com.lazaar.ecommerce.models.ProductStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    // Create Product (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDTO) {
        try {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setUnitPrice(productDTO.getUnitPrice());
            product.setUnitsInStock(productDTO.getUnitInStock());
            product.setActive(productDTO.isActive());

            // Convert Base64 to byte[]
            if (productDTO.getImageBase64() != null && productDTO.getImageContentType() != null) {
                product.setImageData(Base64.getDecoder().decode(productDTO.getImageBase64()));
                product.setImageContentType(productDTO.getImageContentType());
            }

            // Set Category
            ProductCategory category = productCategoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            product.setCategory(category);

            // Set Product Status
            ProductStatus productStatus = productStatusRepository.findById(productDTO.getProductStatusId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));
            product.setProductStatus(productStatus);

            // Sauvegarde via le service
            Product createdProduct = productsService.createProduct(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get products by status (paginated)
    @GetMapping("/bystatus/{id}/{page}/{size}")
    public ResponseEntity<Page<Product>> getProductByStatusId(
            @PathVariable("id") Long id,
            @PathVariable("page") int page,
            @PathVariable("size") int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productsService.getProductByStatusId(id, page, size));
    }

    // Search products by name (paginated)
    @GetMapping("/search/{name}/{page}/{size}")
    public ResponseEntity<Page<Product>> searchProductByName(
            @PathVariable("name") String name,
            @PathVariable("page") int page,
            @PathVariable("size") int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productsService.searchProductByName(name, page, size));
    }

    // Get single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productsService.getProductById(id));
    }

    // Get random products by category
    @GetMapping("/bycategory/random/{id}")
    public ResponseEntity<List<Product>> getRandomProductByCategoryIdLimit(
            @PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productsService.getRandomProductByCategoryIdLimit(id));
    }

    // Get random products by status
    @GetMapping("/bystatus/random/{id}")
    public ResponseEntity<List<Product>> getRandomProductByStatusIdLimit(
            @PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productsService.getProductByStatusIdLimit(id));
    }
}
