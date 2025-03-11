package com.nahudev.electronic_shop.request;

import com.nahudev.electronic_shop.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {

    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private BigDecimal sellingPrice;

    private int inventory;

    private String status;

    private String description;

    private Category category;

}
