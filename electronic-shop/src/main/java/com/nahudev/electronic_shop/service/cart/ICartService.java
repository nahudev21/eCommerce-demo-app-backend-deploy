package com.nahudev.electronic_shop.service.cart;

import com.nahudev.electronic_shop.dto.CartDTO;
import com.nahudev.electronic_shop.model.Cart;
import com.nahudev.electronic_shop.model.User;

import java.math.BigDecimal;

public interface ICartService {

    public Cart getCart(Long cartId);

    public void clearCart(Long cartId);

    public BigDecimal getTotalPrice(Long cartId);

    public Cart initializeNewCart(User user);

    public Cart getCartByUserId(Long userId);

    CartDTO convertToDto(Cart cart);
}
