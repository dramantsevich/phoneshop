package com.es.core.service;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order) throws OrderNotFoundException;

    Order getItemBySecureId(String id);

    List<Order> getOrders();

    Order getOrderById(Long id);

    Order setStatus(Order order, OrderStatus orderStatus);
}
