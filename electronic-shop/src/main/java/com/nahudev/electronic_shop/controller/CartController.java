package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.dto.CartDTO;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Cart;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{userId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            CartDTO cartDTO = cartService.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("Success!", cartDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear cart success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Get total price success!", totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
