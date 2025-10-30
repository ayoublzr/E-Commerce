package com.lazaar.ecommerce.Security;

import com.lazaar.ecommerce.DTO.request.CartDto;
import com.lazaar.ecommerce.DTO.response.DtoCart;

import java.util.List;

public interface CartService {

    String addItemsToCart(CartDto cartDto);

    List<DtoCart> getCartItems(String userId);

    void deleteProduct(String productId, Long userId);
}

