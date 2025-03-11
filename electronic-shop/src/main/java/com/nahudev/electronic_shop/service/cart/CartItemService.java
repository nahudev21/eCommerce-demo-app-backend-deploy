package com.nahudev.electronic_shop.service.cart;

import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Cart;
import com.nahudev.electronic_shop.model.CartItem;
import com.nahudev.electronic_shop.model.Product;
import com.nahudev.electronic_shop.repository.ICartItemRepository;
import com.nahudev.electronic_shop.repository.ICartRepository;
import com.nahudev.electronic_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final ICartItemRepository cartItemRepository;

    private final ICartRepository cartRepository;

    private final ICartService cartService;

    private final IProductService productService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {

        // Traer el Carrito.
        // Traer el Producto.
        // Chequear si el producto ya esta agregado al carrito.
        // Si es asi, incrementar la cantidad con la cantidad solicitada utilizando el cartItem previamente creado.
        // Si no es asi, agregar un nuevo item dentro del carrito creando un nuevo cartItem.

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if(cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getSellingPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);

        cart.removeItem(itemToRemove);
        cartRepository.save(cart);

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {

        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);

        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

}
