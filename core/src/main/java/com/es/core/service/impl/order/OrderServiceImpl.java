package com.es.core.service.impl.order;

import com.es.core.service.OrderService;
import com.es.core.dao.StockDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OrderOutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private long orderId;

    @Value("${delivery.price}")
    private String deliveryPrice;

    private final List<Order> orderList = new ArrayList<>();

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StockDao stockDao;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        List<CartItem> cartItemList = new ArrayList<>();
        CartItem newCartItem;

        for(CartItem cartItem : cart.getItems()) {
            Stock phone = stockDao.getPhoneById(cartItem.getStock().getPhone().getId())
                    .orElseThrow(NullPointerException::new);
            newCartItem = new CartItem(phone, cartItem.getQuantity());

            cartItemList.add(newCartItem);
            order.setItems(cartItemList);
            order.setSubtotal(cart.getTotalCost());
            order.setDeliveryPrice(new BigDecimal(deliveryPrice));
            order.setTotalPrice(order.getSubtotal().add(new BigDecimal(deliveryPrice)));
        }

        return order;
    }

    @Override
    public void placeOrder(Order order) throws OrderNotFoundException {
        order.setSecureId(UUID.randomUUID().toString());
        updateStock(order);
        save(order);

        String pattern = "dd-M-yyyy kk:mm:ss";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        order.setDeliveryDate(date);
        order.setStatus(OrderStatus.NEW);
    }

    private void save(Order item) {
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
                jdbcTemplate.update("update stocks \n" +
                        "set reserved =" + updateReserved +
                        "\n where phoneId=" + id);
            }

            item.getStock().setReserved(updateReserved);
        }
    }

    @Override
    public Order getItemBySecureId(String id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getSecureId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<Order> getOrders() {
        return Optional.of(orderList).get();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order setStatus(Order order, OrderStatus orderStatus) {
        if(orderStatus == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.DELIVERED);
        }

        if(orderStatus == OrderStatus.REJECTED) {
            List<CartItem> orderList = order.getItems();

            for (CartItem item : orderList) {
                long id = item.getStock().getPhone().getId();
                int quantity = item.getQuantity();
                int stock = item.getStock().getStock();
                int reserved = item.getStock().getReserved();
                int updateReserved = reserved - quantity;

                if ((stock - reserved) < quantity) {
                    throw new OrderOutOfStockException();
                } else {
                    jdbcTemplate.update("update stocks \n" +
                            "set reserved =" + updateReserved +
                            "\n where phoneId=" + id);
                }

                item.getStock().setReserved(updateReserved);
                order.setStatus(orderStatus);
            }
        }

        return order;
    }
}
