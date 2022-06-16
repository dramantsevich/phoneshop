package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.cart.CartService;
import com.es.core.model.cart.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/back")
public class ErrorPageController {
    @Autowired
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String backToProductList(Model model,
                                    HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        cartService.clearCart(cart);
        model.addAttribute("cart", cart);

        return "redirect:/productList/1";
    }
}
