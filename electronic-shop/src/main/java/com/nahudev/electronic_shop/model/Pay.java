package com.nahudev.electronic_shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mercadoPagoId;

    private String status;

    private BigDecimal totalPrice;

    private LocalDate payDate;

    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
