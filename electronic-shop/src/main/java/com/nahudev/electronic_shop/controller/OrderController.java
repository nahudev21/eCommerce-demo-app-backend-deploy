package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.dto.OrderDTO;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Order;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order/place-order")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
      try {
          Order order = orderService.placeOrder(userId);
          OrderDTO orderDTO = orderService.convertToDto(order);
          return ResponseEntity.ok(new ApiResponse("Item order success!", orderDTO));
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .body(new ApiResponse("Error ocurred!", e.getMessage()));
      }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order found success!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> userOrdersList = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Order found success!", userOrdersList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
