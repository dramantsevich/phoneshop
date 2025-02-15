package com.es.core.service.impl.order;

import com.es.core.dao.OrderDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.service.OrderService;
import com.es.core.dao.StockDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("${delivery.price}")
    private String deliveryPrice;

    private final StockDao stockDao;

    private final OrderDao orderDao;

    private final DefaultOrderService defaultOrderService;

    @Autowired
    public OrderServiceImpl(StockDao stockDao, OrderDao orderDao, DefaultOrderService defaultOrderService) {
        this.stockDao = stockDao;
        this.orderDao = orderDao;
        this.defaultOrderService = defaultOrderService;
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        List<CartItem> cartItemList = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Stock phone = stockDao.getPhoneById(cartItem.getStock().getPhone().getId())
                    .orElseThrow(PhoneNotFoundException::new);

            cartItemList.add(new CartItem(phone, cartItem.getQuantity()));
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
        defaultOrderService.updateStock(order);
        defaultOrderService.save(order);

        String pattern = "dd-M-yyyy kk:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        order.setDeliveryDate(date);
        order.setStatus(OrderStatus.NEW);
    }

    @Override
    public Order getItemBySecureId(String id) {
        Map<Long, Order> orderMap = defaultOrderService.getOrderMap();
        Order order = null;

        for (Map.Entry<Long, Order> e : orderMap.entrySet()) {
            order = e.getValue();

            if (order.getSecureId().equals(id)) {
                return order;
            }
        }
        return order;
    }

    @Override
    public Map<Long, Order> getOrders() {
        return defaultOrderService.getOrderMap();
    }

    @Override
    public Order getOrderById(Long id) {
        return defaultOrderService.getItem(id);
    }

    @Override
    public Order setStatus(Order order, OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.DELIVERED);
        }

        if (orderStatus == OrderStatus.REJECTED) {
            List<CartItem> orderList = order.getItems();

            for (CartItem item : orderList) {
                long id = item.getStock().getPhone().getId();
                int quantity = item.getQuantity().intValue();
                int reserved = orderDao.getReservedValueById(id);
                int updateReserved = reserved - quantity;

                orderDao.updateReservedValueById(updateReserved, id);

                item.getStock().setReserved(updateReserved);
                order.setStatus(orderStatus);
            }
        }

        return order;
    }
}
