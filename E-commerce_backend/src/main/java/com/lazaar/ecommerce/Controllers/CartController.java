package com.lazaar.ecommerce.Controllers;

import com.lazaar.ecommerce.DTO.request.CartDto;
import com.lazaar.ecommerce.DTO.response.DtoCart;
import com.lazaar.ecommerce.Security.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ✅ Add or update a product in the user's cart
    @PutMapping("/addproduct")
    public ResponseEntity<String> addProductToCart(@RequestBody CartDto cartDto) {
        String response = cartService.addItemsToCart(cartDto);
        return ResponseEntity.ok(response);
    }

    // ✅ Retrieve all items in the user's cart
    @GetMapping("/cartitems/{user_id}")
    public ResponseEntity<List<DtoCart>> getCartItems(@PathVariable("user_id") String userId) {
        List<DtoCart> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    // ✅ Remove a specific product from the cart
    @DeleteMapping("/{product_id}/{item_id}")
    public ResponseEntity<String> deleteProductFromCart(
            @PathVariable("product_id") String productId,
            @PathVariable("item_id") Long userId) {
        cartService.deleteProduct(productId, userId);
        return ResponseEntity.ok("Product removed successfully from the cart.");
    }
}
