package com.es.core.service;

import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.Map;

public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order);

    Order getItemBySecureId(String id);

    Map<Long, Order> getOrders();

    Order getOrderById(Long id);

    Order setStatus(Order order, OrderStatus orderStatus);
}
