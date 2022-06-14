package com.es.core.dao.order;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;

public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order) throws OrderNotFoundException;

    Order getItemBySecureId(String id);
}
