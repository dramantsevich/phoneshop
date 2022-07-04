package com.es.core.dto;

import com.es.core.model.cart.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AjaxResponseDTO {
    private boolean validated;
    private Map<String, String> errorMessages;
    private Cart cart;
}
