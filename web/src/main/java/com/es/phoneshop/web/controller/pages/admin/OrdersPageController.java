package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.dao.order.OrderService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminOrdersPage(Model model) {
        List<Order> orderList = orderService.getOrders();

        model.addAttribute("orders", orderList);

        return "adminOrders";
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public String getAdminOrderDetailPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);

        return "adminOrderDetail";
    }

    @RequestMapping(value = "/delivered/{orderId}", method = RequestMethod.GET)
    public String setDeliveredStatus(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        order = orderService.setStatus(order, OrderStatus.DELIVERED);

        model.addAttribute("order", order);

        return "redirect:/admin/orders/" + orderId;
    }

    @RequestMapping(value = "/rejected/{orderId}", method = RequestMethod.GET)
    public String setRejectedStatus(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        order = orderService.setStatus(order, OrderStatus.REJECTED);

        model.addAttribute("order", order);

        return "redirect:/admin/orders/" + orderId;
    }
}
