package com.nahudev.electronic_shop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private BigDecimal sellingPrice;

    private int inventory;

    private String status;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Product(Long id, String name, String brand, BigDecimal price, BigDecimal sellingPrice,
                   int inventory, String status, String description, Category category) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.inventory = inventory;
        this.status = status;
        this.description = description;
        this.category = category;
    }
}
