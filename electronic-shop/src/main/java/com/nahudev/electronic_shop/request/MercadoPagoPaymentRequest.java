package com.nahudev.electronic_shop.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/* Artículo comprado. */
@Getter
@Builder
public class MercadoPagoPaymentRequest {

  private final String id;

  private final String title;

  private final String description;

  private final String categoryId;

  private final Integer quantity;

  private final BigDecimal unitPrice;

  private final String currencyId;

}
