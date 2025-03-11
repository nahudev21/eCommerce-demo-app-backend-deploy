package com.nahudev.electronic_shop.dto;

import com.nahudev.electronic_shop.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;

    private Long userId;

    private LocalDate orderDate;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private List<OrderItemDTO> orderItems;

}
