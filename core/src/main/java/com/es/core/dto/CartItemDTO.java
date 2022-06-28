package com.es.core.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    @Min(value = 1, message = "enter number")
    private Long quantity;
    private int totalQuantity;
    private BigDecimal totalCost;
}
