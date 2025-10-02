package com.lazaar.ecommerce.Services.implSecurity;

import com.lazaar.ecommerce.Repositories.ProductStatusRepository;
import com.lazaar.ecommerce.Security.ProductStatusService;
import com.lazaar.ecommerce.models.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductStatusServiceImpl implements ProductStatusService {

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Override
    public List<ProductStatus> getProductStatusList() {
        // Récupère la liste complète des statuts de produits
        return productStatusRepository.findAll();
    }

    @Override
    public ProductStatus saveProductStatus(ProductStatus productStatus) {
        // Sauvegarde un statut de produit (création ou mise à jour)
        return productStatusRepository.save(productStatus);
    }
}