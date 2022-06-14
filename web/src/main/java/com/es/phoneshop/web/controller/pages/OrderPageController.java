package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.cart.CartService;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.dao.order.DefaultOrderService;
import com.es.core.dao.order.OrderService;
import com.es.core.exception.OrderNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
    public void getOrder(HttpServletRequest request, Model model) throws OrderNotFoundException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("order", order);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(HttpServletRequest request, Model model) throws OrderNotFoundException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);
        Map<String, String> errors = new HashMap<>();

        defaultOrderService.setRequiredParameter(request, "firstName", errors, order::setFirstName);
        defaultOrderService.setRequiredParameter(request, "lastName", errors, order::setLastName);
        defaultOrderService.setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
        defaultOrderService.setRequiredParameter(request, "contactPhoneNo", errors, order::setContactPhoneNo);

        checkoutError(cart, errors, order, model);

        if (errors.isEmpty()) {
            return "redirect:/orderOverview/" + order.getSecureId();
        } else {
            return "order";
        }
    }

    private void checkoutError(Cart cart, Map<String, String> errors, Order order, Model model) {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(cart);
        } else {
            model.addAttribute("cart", cart);
            model.addAttribute("errors", errors);
            model.addAttribute("order", order);
        }
    }
}
