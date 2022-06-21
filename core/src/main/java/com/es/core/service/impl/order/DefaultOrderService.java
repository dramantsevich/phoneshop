package com.es.core.service.impl.order;

import com.es.core.dao.OrderDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OrderOutOfStockException;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class DefaultOrderService {
    @Autowired
    private OrderDao orderDao;

    private long orderId;

    final List<Order> orderList = new ArrayList<>();

    void save(Order item) {
        Long longId = item.getId();

        if (longId != null) {
            Long id = item.getId();

            orderList.remove(getItem(id));
        } else {
            item.setId(++orderId);
        }

        orderList.add(item);
    }

    private Order getItem(Long id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    public void updateStock(Order order) {
        List<CartItem> orderList = order.getItems();

        for (CartItem item : orderList) {
            long id = item.getStock().getPhone().getId();
            int quantity = item.getQuantity();
            int stock = item.getStock().getStock();
            int reserved = item.getStock().getReserved();
            int updateReserved = quantity + reserved;

            if ((stock - reserved) < quantity) {
                throw new OrderOutOfStockException();
            } else {
                orderDao.update(updateReserved, id);
            }

            item.getStock().setReserved(updateReserved);
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
