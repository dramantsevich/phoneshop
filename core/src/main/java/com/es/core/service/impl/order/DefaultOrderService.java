package com.es.core.service.impl.order;

import com.es.core.dao.OrderDao;
import com.es.core.exception.OrderOutOfStockException;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Service
public class DefaultOrderService {
    private final OrderDao orderDao;

    @Autowired
    public DefaultOrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    private final AtomicLong orderId = new AtomicLong(0);

    private final Map<AtomicLong, Order> orderMap = new HashMap();

    public Map<AtomicLong, Order> getOrderMap() {
        return orderMap;
    }

    public void save(Order item) {
        long id = orderId.incrementAndGet();

        item.setId(id);

        orderMap.put(new AtomicLong(id), item);
    }

    public Order getItem(Long id) {
        Order order = null;

        for (Map.Entry<AtomicLong, Order> e : orderMap.entrySet()) {
            order = e.getValue();

            if (order.getId().equals(id)) {
                return order;
            }
        }

        return order;
    }

    @Transactional
    public void updateStock(Order order) {
        List<CartItem> cartItemList = order.getItems();

        for (CartItem item : cartItemList) {
            long id = item.getStock().getPhone().getId();
            int quantity = item.getQuantity().intValue();
            int stock = orderDao.getStockValueById(id);
            int reserved = orderDao.getReservedValueById(id);
            int updateReserved = quantity + reserved;

            if ((stock - reserved) < quantity) {
                throw new OrderOutOfStockException();
            } else {
                orderDao.updateReservedValueById(updateReserved, id);
            }

            item.getStock().setReserved(updateReserved * item.getStock().getPhone().getColor().size());
        }
    }

    public void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            String errorMessage = "Value is required";
            errors.put(parameter, errorMessage);
        } else {
            consumer.accept(value);
        }
    }
}
