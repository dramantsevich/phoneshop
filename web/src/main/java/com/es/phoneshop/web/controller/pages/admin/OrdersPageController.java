package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.service.OrderService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getAdminOrdersPage(Model model) {
        Map<Long, Order> orderMap = orderService.getOrders();

        model.addAttribute("orders", orderMap);

        return "adminOrders";
    }

    @GetMapping(value = "/{orderId}")
    public String getAdminOrderDetailPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);

        return "adminOrderDetail";
    }

    @GetMapping(value = "/delivered/{orderId}")
    public String setDeliveredStatus(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        order = orderService.setStatus(order, OrderStatus.DELIVERED);

        model.addAttribute("order", order);

        return "redirect:/admin/orders/" + orderId;
    }

    @GetMapping(value = "/rejected/{orderId}")
    public String setRejectedStatus(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        order = orderService.setStatus(order, OrderStatus.REJECTED);

        model.addAttribute("order", order);

        return "redirect:/admin/orders/" + orderId;
    }
}
