package com.lazaar.ecommerce.Services.implSecurity;

import com.lazaar.ecommerce.DTO.request.WishListDto;
import com.lazaar.ecommerce.Repositories.WishListRepository;
import com.lazaar.ecommerce.Security.ProductsService;
import com.lazaar.ecommerce.models.Product;
import com.lazaar.ecommerce.models.WishList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductsService productsService;

    public List<WishList> getALLProductByUserWishListId(Long id) {
        // Récupère tous les produits d’une wish list utilisateur par son ID
        return wishListRepository.findAllWishListById(id);
    }

    public void addProductToWishListByUser(WishListDto wishListDto) {
        // Fetch the product using the product ID from the DTO
        Optional<Product> productOptional = productsService.getProductById(wishListDto.getProductId());

        // Check if the product is present
        if (productOptional.isPresent()) {
            // extrait l'objet Product réel contenu dans l'Optional,
            // permettant de continuer à travailler avec cet objet.
            Product product = productOptional.get();
            log.info("Product found for userId :: " + wishListDto.getUserId());

            // Build the WishList object
            WishList wishListInstance = WishList.builder()
                    .userId(wishListDto.getUserId())
                    .product(product) // pass the actual product object, not the Optional
                    .build();

            // Save the WishList to the repository
            wishListRepository.save(wishListInstance);
            log.info("Product added to wishList successfully!");
        } else {
            log.error("Product with ID " + wishListDto.getProductId() +
                    " not found for userId " + wishListDto.getUserId());
            throw new RuntimeException("Product not found");
        }
    }
}