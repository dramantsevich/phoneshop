package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.NegativeQuantityException;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhonePriceException;
import com.es.core.exception.QuantityNullException;
import com.es.core.model.cart.Cart;
import com.es.core.dao.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateCart(@RequestParam Long[] quantity,
                             @RequestParam Long[] productId,
                             Model model,
                             HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productId.length; i++) {
            try {
                cartService.update(cart, productId[i], quantity[i]);
            } catch (QuantityNullException | PhonePriceException | OutOfStockException | NegativeQuantityException ex) {
                handleUpdateErrors(errors, productId[i], ex);
            }
        }
        model.addAttribute("cart", cart);

        if(errors.isEmpty()){
            return "redirect:/cart";
        } else{
            model.addAttribute("errors", errors);
            return "cart";
        }
    }

    private void handleUpdateErrors(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(QuantityNullException.class)) {
            errors.put(productId, "Field quantity is empty");
        } else if (e.getClass().equals(PhonePriceException.class)) {
            errors.put(productId, "Price is null, not available now");
        } else if (e.getClass().equals(OutOfStockException.class)) {
            errors.put(productId, "No such quantity available");
        } else if (e.getClass().equals(NegativeQuantityException.class)) {
            errors.put(productId, "Should be grater then 0");
        }
    }
}
