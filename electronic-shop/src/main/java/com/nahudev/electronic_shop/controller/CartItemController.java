package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Cart;
import com.nahudev.electronic_shop.model.User;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.cart.ICartItemService;
import com.nahudev.electronic_shop.service.cart.ICartService;
import com.nahudev.electronic_shop.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;

    private final ICartService cartService;

    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {

        try {

            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add product to cart success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new  ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemToCart(@PathVariable Long cartId,
                                                        @PathVariable Long itemId) {

        try {
          cartItemService.removeItemFromCart(cartId, itemId);
          return ResponseEntity.ok(new ApiResponse("Removed item success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                     @PathVariable Long itemId,
                                                     @RequestParam Integer quantity) {

        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Updated item success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
