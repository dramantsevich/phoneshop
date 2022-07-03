package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {

    Cart getCart(HttpServletRequest request);

    void addPhone(Cart cart, Long phoneId, Long quantity) throws OutOfStockException;

    void update(Cart cart, Long phoneId, Long quantity);

    void remove(Cart cart, Long phoneId);

    void clearCart(Cart cart);
}
