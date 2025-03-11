package com.nahudev.electronic_shop.dto;

import com.nahudev.electronic_shop.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private BigDecimal sellingPrice;

    private int inventory;

    private String status;

    private String description;

    private Category category;

    private List<ImageDTO> images;

}
