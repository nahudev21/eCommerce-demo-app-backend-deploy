package com.nahudev.electronic_shop.service.cart;

import com.nahudev.electronic_shop.model.CartItem;

public interface ICartItemService {

    public void addItemToCart(Long cartId, Long productId, int quantity);

    public void removeItemFromCart(Long cartId, Long productId);

    public void updateItemQuantity(Long cartId, Long productId, int quantity);

    public CartItem getCartItem(Long cartId, Long productId);
}
