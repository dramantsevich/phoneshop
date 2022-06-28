package com.es.phoneshop.integration;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.service.OrderService;
import com.es.core.exception.OrderOutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/testContext.xml")
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);

        Order order = orderService.createOrder(cart);

        assertThat(order).isNotNull();
    }

    @Test
    public void testPlaceOrder() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        int oldReservedCount = order.getItems().get(0).getStock().getReserved();
        orderService.placeOrder(order);

        assertThat(order.getId()).isPositive();
        assertThat(order.getSecureId()).isNotNull();
        assertThat(order.getDeliveryDate()).isNotNull();
        assertThat(order.getItems().get(0).getStock().getReserved()).isGreaterThan(oldReservedCount);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    public void testPlaceOrderThrowOrderOutOfStockException() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItemWithHugeAmountQuantity(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        assertThatThrownBy(() -> {
            orderService.placeOrder(order);
        }).isInstanceOf(OrderOutOfStockException.class);
    }

    @Test
    public void testGetItemBySecureId() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        orderService.placeOrder(order);
        Order expectedOrder = orderService.getItemBySecureId(order.getSecureId());

        assertThat(expectedOrder).isNotNull();
    }

    @Test
    public void testGetOrders() {
        Map<Long, Order> orderList = orderService.getOrders();
        orderList.clear();

        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        orderService.placeOrder(order);

        assertThat(orderList).hasSize(1);
    }

    @Test
    public void testGetOrderById() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        orderService.placeOrder(order);
        Order expectedOrder = orderService.getOrderById(1L);

        assertThat(expectedOrder).isNotNull();
    }

    @Test
    public void testSetDeliveredStatus() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        orderService.placeOrder(order);
        orderService.setStatus(order, OrderStatus.DELIVERED);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    public void testSetRejectedStatus() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItem(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        int oldReservedCount = order.getItems().get(0).getStock().getReserved();
        orderService.placeOrder(order);
        int newCountAfterPlacedOrder = order.getItems().get(0).getStock().getReserved();
        orderService.setStatus(order, OrderStatus.REJECTED);
        int expectedReservedCount = order.getItems().get(0).getStock().getReserved();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.REJECTED);
        assertThat(newCountAfterPlacedOrder).isGreaterThan(oldReservedCount);
        assertThat(expectedReservedCount).isEqualTo(oldReservedCount);
    }

    @Test
    public void testSetRejectedStatusThrowOrderOutOfStockException() {
        Phone phone = createPhoneWithPrice();
        List<CartItem> cartItemList = createCartItemWithHugeAmountQuantity(phone);
        Cart cart = createCart(cartItemList);
        Order order = orderService.createOrder(cart);

        assertThatThrownBy(() -> {
            orderService.placeOrder(order);
        }).isInstanceOf(OrderOutOfStockException.class);

        orderService.setStatus(order, OrderStatus.REJECTED);
    }

    private Cart createCart(List<CartItem> cartItemList) {
        Cart cart = new Cart();
        cart.setItems(cartItemList);
        cart.setTotalQuantity(2);
        try {
            cart.setTotalCost(cartItemList.get(0).getStock().getPhone()
                    .getPrice().multiply(BigDecimal.valueOf(cart.getTotalQuantity())));
        } catch (NullPointerException | PhoneNotFoundException ex) {
            cart.setTotalCost(BigDecimal.ZERO);
        }

        return cart;
    }

    private List<CartItem> createCartItem(Phone phone) {
        List<CartItem> cartItemList = new ArrayList<>();

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(11);
        stock.setReserved(0);

        cartItemList.add(new CartItem(stock, 2L));

        return cartItemList;
    }

    private List<CartItem> createCartItemWithHugeAmountQuantity(Phone phone) {
        List<CartItem> cartItemList = new ArrayList<>();

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(11);
        stock.setReserved(0);

        cartItemList.add(new CartItem(stock, 100L));

        return cartItemList;
    }

    private Phone createPhoneWithPrice() {
        Phone phone = new Phone();
        phone.setId(1006L);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 101 XS 2");
        phone.setPrice(new BigDecimal("270.0"));

        return phone;
    }
}
