package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.OutOfStockException;
import com.es.core.dto.CartItemDTO;
import com.es.core.validation.cartitem.CartItemValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    private final CartItemValidator validator;

    @Resource
    private CartService cartService;

    @InitBinder("cartItemDTO")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }


    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> addPhone(@Valid @RequestBody CartItemDTO cartItemDTO, Errors errors,
                                      HttpServletRequest request) throws OutOfStockException {
        Cart cart = cartService.getCart(request);
        //these methods are used in further development
        //cartService.addPhone(cart, cartItemDTO.getId(), cartItemDTO.getQuantity().longValue());
        //request.setAttribute("cart", cart);

        return ResponseEntity.ok(errors.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getCode).collect(Collectors.joining("; ")));
    }
}