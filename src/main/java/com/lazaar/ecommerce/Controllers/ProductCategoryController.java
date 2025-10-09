package com.lazaar.ecommerce.Controllers;

import com.lazaar.ecommerce.Services.implSecurity.ProductCategoryServiceImpl;
import com.lazaar.ecommerce.models.ProductCategory;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Allow only your Angular app's origin
@RequestMapping("/productcategory")
@AllArgsConstructor
public class ProductCategoryController {

    private ProductCategoryServiceImpl productCategoryService;

    @GetMapping
    public List<ProductCategory> getProductCategoryList() {
        // Récupère la liste de toutes les catégories de produits
        return productCategoryService.getProductCategoryList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductCategory createProductCategory(@RequestBody ProductCategory productCategory) {
        // Crée une nouvelle catégorie de produit (réservé aux ADMINs)
        return productCategoryService.createCategory(productCategory);
    }

    // Update an existing product category by ID
    @PutMapping("/{id}")
    public ProductCategory updateProductCategory(
            @PathVariable Long id,
            @RequestBody ProductCategory productCategory) {
        // Met à jour une catégorie existante avec l’ID donné
        return productCategoryService.updateCategory(id, productCategory);
    }

    // Delete a product category by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProductCategory(@PathVariable Long id) {
        // Supprime une catégorie par son ID (réservé aux ADMINs)
        productCategoryService.deleteCategory(id);
        return "Category with ID " + id + " has been deleted.";
    }
}
