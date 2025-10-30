package com.lazaar.ecommerce.Services.implSecurity;

import com.lazaar.ecommerce.DTO.request.CartDto;
import com.lazaar.ecommerce.DTO.response.DtoCart;
import com.lazaar.ecommerce.Repositories.CartItemsRepository;
import com.lazaar.ecommerce.Repositories.CartRepository;
import com.lazaar.ecommerce.Security.CartService;
import com.lazaar.ecommerce.models.Cart;
import com.lazaar.ecommerce.models.CartItems;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    @Transactional
    @Override
    public String addItemsToCart(CartDto cartDto) {
        // Check if the user already has a cart
        Optional<Cart> optionalCart = cartRepository.findByUserId(cartDto.getUserId());
        Cart cart;
        CartItems cartItems;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();

            // Check if the product already exists in the user's cart
            Optional<CartItems> optionalCartItems = cartItemsRepository
                    .findByCartAndProductId(cart, cartDto.getProductId());

            if (optionalCartItems.isPresent()) {
                // Increment the quantity and update the price if the product exists in the cart
                cartItems = optionalCartItems.get();
                cartItems.setProductQuantity(cartItems.getProductQuantity() + 1);
                cartItems.setProductPrice(cartItems.getProductPrice() * 2);
                cartItemsRepository.save(cartItems);
                log.info("Product quantity updated in the cart.");
            } else {
                // Add a new product to the cart
                cartItems = CartItems.builder()
                        .productImg(cartDto.getProductImg())
                        .productId(cartDto.getProductId())
                        .productPrice(cartDto.getProductPrice())
                        .productName(cartDto.getProductName())
                        .productQuantity(cartDto.getProductQuantity())
                        .cart(cart)
                        .build();
                cartItemsRepository.save(cartItems);
                log.info("New product added to the cart.");
            }
        } else {
            // Create a new cart for the user if it doesn't exist
            cart = Cart.builder()
                    .userId(cartDto.getUserId())
                    .productTotalPrice(cartDto.getProductPrice())
                    .productTotalQuantity(cartDto.getProductQuantity())
                    .build();

            cartItems = CartItems.builder()
                    .productImg(cartDto.getProductImg())
                    .productId(cartDto.getProductId())
                    .productPrice(cartDto.getProductPrice())
                    .productName(cartDto.getProductName())
                    .productQuantity(cartDto.getProductQuantity())
                    .cart(cart)
                    .build();

            List<CartItems> cartItemsList = new ArrayList<>();
            cartItemsList.add(cartItems);
            cart.setCartItemsList(cartItemsList);
            cartRepository.save(cart);
            log.info("New cart created and product added.");
        }

        return "Item added to cart successfully.";
    }

    @Override
    public List<DtoCart> getCartItems(String userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            // Fetch items in the cart
            Optional<List<CartItems>> optionalCartItems = cartItemsRepository.findByCart(cart);
            if (optionalCartItems.isPresent()) {
                List<CartItems> cartItems = optionalCartItems.get();

                // Convert CartItems to DtoCart for response
                return cartItems.stream()
                        .map(item -> new DtoCart(
                                item.getId(),
                                item.getProductName(),
                                item.getProductId(),
                                item.getProductImg(),
                                item.getProductQuantity(),
                                item.getProductPrice()
                        ))
                        .collect(Collectors.toList());
            } else {
                log.warn("No items found in the cart for userId: " + userId);
                return new ArrayList<>(); // Return empty list if no items
            }
        } else {
            log.error("Cart not found for userId: " + userId);
            return new ArrayList<>(); // Return empty list if no cart found
        }
    }

    @Transactional
    @Override
    public void deleteProduct(String productId, Long userId) {
        cartItemsRepository.deleteByIdAndProductId(userId, productId);
        log.info("Product with id " + productId + " removed from cart for user " + userId);
    }
}
