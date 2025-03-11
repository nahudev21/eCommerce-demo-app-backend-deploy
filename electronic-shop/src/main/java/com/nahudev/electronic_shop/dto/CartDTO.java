package com.nahudev.electronic_shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartDTO {

    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private Set<CartItemDTO> items = new HashSet<>();

}
