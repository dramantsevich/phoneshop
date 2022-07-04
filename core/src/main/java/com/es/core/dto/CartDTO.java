package com.es.core.dto;

import com.es.core.model.phone.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long itemId;
    private Stock stock;

    @NotNull(message = "Not null")
    @Positive(message = "Value must be greater than 0")
    private Long quantity;

    public CartDTO(Long itemId, Stock stock) {
        this.itemId = itemId;
        this.stock = stock;
    }
}
