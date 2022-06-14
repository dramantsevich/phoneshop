package com.es.core.dao.order;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OrderOutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private long orderId;

    @Value("${delivery.price}")
    private String deliveryPrice;

    private final List<Order> orderList = new ArrayList<>();

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(CartItem::new).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(new BigDecimal(deliveryPrice));
        order.setTotalPrice(order.getSubtotal().add(new BigDecimal(deliveryPrice)));

        return order;
    }

    @Override
    public void placeOrder(Order order) throws OrderNotFoundException {
        order.setSecureId(UUID.randomUUID().toString());
        updateStock(order);
        save(order);

        order.setStatus(OrderStatus.NEW);
    }

    public void save(Order item) {
        Long longId = item.getId();

        if (longId != null) {
            Long id = item.getId();

            orderList.remove(getItem(id));
        } else {
            item.setId(++orderId);
        }

        orderList.add(item);
    }

    public Order getItem(Long id) {
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
                jdbcTemplate.update("update stocks \n" +
                        "set reserved =" + updateReserved +
                        "\n where phoneId=" + id);
            }
        }
    }

    @Override
    public Order getItemBySecureId(String id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getSecureId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }
}
