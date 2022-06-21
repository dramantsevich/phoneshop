package com.es.core.validation.cartitem;

import com.es.core.dto.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class CartItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CartItemDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CartItemDTO cartItemDTO = (CartItemDTO) o;

        if (cartItemDTO.getQuantity() == null) {
            errors.rejectValue("quantity", "field must not be empty");
        } else if (Integer.parseInt(cartItemDTO.getQuantity()) <= 0) {
            errors.rejectValue("quantity", "quantity should be greater than 0");
        }
    }
}
