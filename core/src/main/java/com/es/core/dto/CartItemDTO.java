package com.es.core.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    @NotNull(message = "Not null")
    @Positive(message = "Value must be greater than 0")
    private Long quantity;
    private int totalQuantity;
    private BigDecimal totalCost;
}
