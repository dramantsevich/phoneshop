package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import com.es.core.model.cart.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/back")
public class ErrorPageController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String backToProductList(Model model,
                                    HttpServletRequest request) {
        Cart cart = cartService.getCart(request);

        model.addAttribute("cart", cart);

        return "redirect:/productList/1";
    }
}
