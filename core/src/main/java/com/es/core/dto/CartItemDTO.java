package com.es.core.dto;

import com.es.core.validation.cartitem.QuantityValidation;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;

    @QuantityValidation(message = "Invalid: must be number and greater than 0")
    private Integer quantity;
}
