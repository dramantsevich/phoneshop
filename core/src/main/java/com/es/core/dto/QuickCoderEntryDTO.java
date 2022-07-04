package com.es.core.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class QuickCoderEntryDTO {
    @NotNull(message = "Product not found, enter number")
    @Positive(message = "Value must be greater than 0")
    private Long id;
    @NotNull(message = "Not null")
    @Positive(message = "Value must be greater than 0")
    private Long quantity;
}
