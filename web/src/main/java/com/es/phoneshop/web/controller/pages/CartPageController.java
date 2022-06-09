package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.dao.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public void getCart(HttpServletRequest request, Model model) {
        Cart cart = cartService.getCart(request);
        model.addAttribute("cart", cart);
    }

    @RequestMapping(value = "/{phoneId}", method = RequestMethod.POST)
    public String deleteCartItem(@PathVariable Long phoneId, Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        cartService.remove(cart, phoneId);
        model.addAttribute("cart", cart);

        return "redirect:/cart";
    }
}
