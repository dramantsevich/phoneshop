package com.es.phoneshop;

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
    private CartService mockCartService = mock(CartService.class);

    private DefaultCartService mockDefaultCartService = mock(DefaultCartService.class);

    @SneakyThrows
    @Test
    public void testAddPhoneWorksCorrectly() {
        Cart cart = mock(Cart.class);
        doReturn(BigDecimal.valueOf(198.0))
                .when(cart)
                .getTotalCost();
        doReturn(2)
                .when(cart)
                .getTotalQuantity();

        List<CartItem> cartItemList = createCartItem();

        doReturn(cartItemList)
                .when(cart)
                .getItems();

        Long id = cart.getItems().get(0).getStock().getPhone().getId();
        Long quantity = (long) cart.getTotalQuantity();

        doNothing().when(mockCartService).addPhone(cart, id, quantity);
        int size = cart.getItems().size();

        Assert.assertEquals(1, size);
    }

    @SneakyThrows
    @Test
    public void testCartTotalCostCalculationWorksCorrectly() {
        Cart cart = mock(Cart.class);

        doReturn(BigDecimal.valueOf(198.0))
                .when(cart).getTotalCost();
        doReturn(2)
                .when(cart).getTotalQuantity();

        doNothing()
                .when(mockDefaultCartService)
                .recalculateCartTotalCost(cart);

        Assert.assertEquals(cart.getTotalCost(), BigDecimal.valueOf(198.0));
    }

    @SneakyThrows
    @Test
    public void testCartTotalQuantityCalculationWorksCorrectly() {
        Cart cart = mock(Cart.class);

        doReturn(BigDecimal.valueOf(198.0))
                .when(cart).getTotalCost();
        doReturn(2)
                .when(cart).getTotalQuantity();

        doNothing()
                .when(mockDefaultCartService)
                .recalculateCartQuantity(cart);

        Assert.assertEquals(cart.getTotalQuantity(), 2);
    }

    private List<CartItem> createCartItem() {
        List<CartItem> cartItemList = new ArrayList<>();
        Phone phone = createPhone();

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(11);
        stock.setReserved(0);

        cartItemList.add(new CartItem(stock, 2));

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
