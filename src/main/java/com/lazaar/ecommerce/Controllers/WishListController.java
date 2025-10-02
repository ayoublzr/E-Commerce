package com.lazaar.ecommerce.Controllers;

import com.lazaar.ecommerce.DTO.request.WishListDto;
import com.lazaar.ecommerce.Services.implSecurity.WishListService;
import com.lazaar.ecommerce.models.WishList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // Ajouter un produit à la wishlist d’un utilisateur
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String addProductToWishList(@RequestBody WishListDto wishListDto) {
        wishListService.addProductToWishListByUser(wishListDto);
        return "Product Added to Wishlist successfully";
    }

    // Récupérer tous les produits d’une wishlist utilisateur
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{userId}")
    public List<WishList> getALLProductByUserWishListId(
            @PathVariable(value = "userId") Long userId) {
        return wishListService.getALLProductByUserWishListId(userId);
    }
}
