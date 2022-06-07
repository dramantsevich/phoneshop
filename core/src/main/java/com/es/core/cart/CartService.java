package com.es.core.cart;

import com.es.core.exception.OutOfStockException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CartService {

    Cart getCart(HttpServletRequest request);

    void addPhone(Cart cart, Long phoneId, Long quantity) throws OutOfStockException;

    /**
     * @param items key: {@link com.es.core.model.phone.Phone#id}
     *              value: quantity
     */
    void update(Map<Long, Long> items);

    void remove(Long phoneId);
}
