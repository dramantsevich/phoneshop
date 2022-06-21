package com.es.core.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    private String quantity;
    private int totalQuantity;
    private BigDecimal totalCost;
}
