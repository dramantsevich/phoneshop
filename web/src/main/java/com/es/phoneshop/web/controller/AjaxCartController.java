package com.es.phoneshop.web.controller;

import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.core.exception.OutOfStockException;
import com.es.core.dto.CartItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @PostMapping (produces = "application/json", consumes = "application/json")
    public ResponseEntity<Cart> addPhone(@Valid @RequestBody CartItemDTO cartItemDTO,
                                      Model model,
                                      HttpServletRequest request) throws OutOfStockException {
        Cart cart = cartService.getCart(request);

        cartService.addPhone(cart, cartItemDTO.getId(), cartItemDTO.getQuantity());

        model.addAttribute("cart", cart);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}