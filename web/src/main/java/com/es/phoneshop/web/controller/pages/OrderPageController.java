package com.es.phoneshop.web.controller.pages;

import com.es.core.dto.OrderDTO;
import com.es.core.service.CartService;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.service.impl.order.DefaultOrderService;
import com.es.core.service.OrderService;
import com.es.core.exception.OrderNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;

    @Resource
    private CartService cartService;

    @Resource
    private DefaultOrderService defaultOrderService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(HttpServletRequest request, Model model) throws OrderNotFoundException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("order", order);
        model.addAttribute("orderDTO", new OrderDTO());

        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@ModelAttribute("orderDTO") @Valid OrderDTO orderDTO, BindingResult result,
                             HttpServletRequest request, Model model) {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);

        if (result.hasErrors()) {
            model.addAttribute("cart", cart);
            model.addAttribute("order", order);

            return "order";
        } else {
            orderService.placeOrder(order);
            cartService.clearCart(cart);

            return "redirect:/orderOverview/" + order.getSecureId();
        }
    }
}
