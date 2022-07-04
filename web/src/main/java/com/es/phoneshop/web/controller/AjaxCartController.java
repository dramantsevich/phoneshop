package com.es.phoneshop.web.controller;

import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.core.dto.CartItemDTO;
import com.es.core.dto.AjaxResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @PostMapping
    public AjaxResponseDTO addPhone(@Valid @RequestBody CartItemDTO cartItemDTO, BindingResult result,
                                    HttpServletRequest request) {
        AjaxResponseDTO ajaxResponse = new AjaxResponseDTO();

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(
                            Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
                    );

            ajaxResponse.setValidated(false);
            ajaxResponse.setErrorMessages(errors);
        } else {
            Cart cart = cartService.getCart(request);

            cartService.addPhone(cart, cartItemDTO.getId(), cartItemDTO.getQuantity());

            ajaxResponse.setValidated(true);
            ajaxResponse.setCart(cart);
        }

        return ajaxResponse;
    }
}