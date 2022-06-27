package com.es.phoneshop.unit;

import com.es.core.service.CartService;
import com.es.core.service.impl.cart.DefaultCartService;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CartTest {
    private final CartService mockCartService = mock(CartService.class);
    private final DefaultCartService mockDefaultCartService = mock(DefaultCartService.class);

    @SneakyThrows
    @Test
    public void testAddPhoneWorksCorrectly() {
        Cart cart = mock(Cart.class);

        List<CartItem> cartItemList = createCartItem(createPhone());

        doReturn(cartItemList)
                .when(cart)
                .getItems();

        Long id = cart.getItems().get(0).getStock().getPhone().getId();
        Long quantity = (long) cart.getTotalQuantity();

        doNothing().when(mockCartService).addPhone(isA(Cart.class), isA(Long.class), isA(Long.class));
        mockCartService.addPhone(cart, id, quantity);
        verify(mockCartService, times(1)).addPhone(cart, id, quantity);

        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);
            Object arg2 = invocation.getArgument(2);

            Assert.assertEquals(cart, arg0);
            Assert.assertEquals(id, arg1);
            Assert.assertEquals(quantity, arg2);
            return null;
        }).when(mockCartService).addPhone(any(Cart.class), any(Long.class), any(Long.class));
        mockCartService.addPhone(cart, id, quantity);

        int size = cart.getItems().size();

        Assert.assertEquals(1, size);
    }

    @SneakyThrows
    @Test
    public void testCartTotalCostCalculationWorksCorrectly() {
        Cart cart = mock(Cart.class);

        doNothing().when(mockDefaultCartService).recalculateCartTotalCost(cart);
        mockDefaultCartService.recalculateCartTotalCost(cart);

        verify(mockDefaultCartService, times(1)).recalculateCartTotalCost(cart);
    }

    @SneakyThrows
    @Test
    public void testCartTotalQuantityCalculationWorksCorrectly() {
        Cart cart = mock(Cart.class);

        doNothing().when(mockDefaultCartService).recalculateCartQuantity(cart);
        mockDefaultCartService.recalculateCartQuantity(cart);

        verify(mockDefaultCartService, times(1)).recalculateCartQuantity(cart);
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

    private Phone createPhone() {
        Phone phone = new Phone();
        phone.setId(1011L);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 40 Cesium");
        phone.setPrice(new BigDecimal("99.0"));

        return phone;
    }
}
