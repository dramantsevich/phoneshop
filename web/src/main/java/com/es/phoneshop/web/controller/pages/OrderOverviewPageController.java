package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.cart.CartService;
import com.es.core.model.cart.Cart;
import com.es.core.dao.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private CartService cartService;

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/{secureId}", method = RequestMethod.GET)
    public String getOrderOverview(@PathVariable String secureId, Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);

        model.addAttribute("cart", cart);
        model.addAttribute("order", orderService.getItemBySecureId(secureId));

        return "orderOverview";
    }
}
