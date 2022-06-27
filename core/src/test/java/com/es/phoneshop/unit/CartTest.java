package com.es.phoneshop.unit;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.model.cart.Cart;
import com.es.core.service.impl.cart.DefaultCartService;
import com.es.core.service.impl.cart.HttpSessionCartServiceImpl;
import lombok.SneakyThrows;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CartTest {
    private final Long phoneId = 1011L;

    private final DefaultCartService mockDefaultCartService = mock(DefaultCartService.class);
    private final CartService cartService = new HttpSessionCartServiceImpl(mockDefaultCartService);

    @SneakyThrows
    @Test
    public void testAddPhoneWithNotEmptyCart() {
        Phone phone = createPhoneWithPrice();
        Optional<CartItem> cartItemOptional = getOptionalCartItem(1L);
        Cart cart = createNotEmptyCart(phone, 1L);

        doReturn(cartItemOptional).when(mockDefaultCartService).findCartItemForUpdate(cart, phoneId, 1L);
        doReturn(createStock(phone)).when(mockDefaultCartService).getPhone(phoneId, 1L);
        cartService.addPhone(cart, phoneId, 1L);

        doReturn(Optional.empty()).when(mockDefaultCartService).findCartItemForUpdate(cart, 1006L, 2L);
        doReturn(createStock(createAnotherPhoneWithPrice())).when(mockDefaultCartService).getPhone(1006L, 2L);
        cartService.addPhone(cart, 1006L, 2L);

        assertThat(cart.getItems()).hasSize(2);
    }

    @SneakyThrows
    @Test
    public void testAddPhoneWithEmptyCart() {
        Phone phone = createPhoneWithPrice();
        Optional<CartItem> cartItemOptional = Optional.empty();
        Cart cart = new Cart();

        doReturn(cartItemOptional).when(mockDefaultCartService).findCartItemForUpdate(cart, phoneId, 1L);
        doReturn(createStock(phone)).when(mockDefaultCartService).getPhone(phoneId, 1L);
        cartService.addPhone(cart, phoneId, 1L);

        assertThat(cart.getItems()).hasSize(1);
    }

    @SneakyThrows
    @Test
    public void testAddPhoneThrowOutOfStockException() {
        Phone phone = createPhoneWithPrice();
        Optional<CartItem> cartItemOptional = getOptionalCartItem(4L);
        Cart cart = createNotEmptyCart(phone, 4L);

        doReturn(cartItemOptional).when(mockDefaultCartService).findCartItemForUpdate(cart, phoneId, 4L);
        doReturn(createStock(phone)).when(mockDefaultCartService).getPhone(phoneId, 4L);

        assertThatThrownBy(() -> {
            cartService.addPhone(cart, phoneId, 4L);
        }).isInstanceOf(OutOfStockException.class);
    }

    private Optional<CartItem> getOptionalCartItem(Long quantity) {
        return Optional.of(createCartItem(createPhoneWithPrice(), quantity).get(0));
    }

    private Cart createNotEmptyCart(Phone phone, Long quantity) {
        Cart cart = new Cart();
        List<CartItem> cartItemList = new ArrayList<>();

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(11);
        stock.setReserved(0);

        cartItemList.add(new CartItem(stock, quantity));
        cart.setItems(cartItemList);

        return cart;
    }

    private Stock createStock(Phone phone) {
        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(11);
        stock.setReserved(0);

        return stock;
    }

    private List<CartItem> createCartItem(Phone phone, Long quantity) {
        List<CartItem> cartItemList = new ArrayList<>();

        Stock stock = createStock(phone);

        cartItemList.add(new CartItem(stock, quantity));

        return cartItemList;
    }

    private Phone createPhoneWithPrice() {
        Set<Color> colorSet = new HashSet<>();
        Color white = new Color(1001L, "White");
        Color black = new Color(1002L, "Yellow");
        Color blue = new Color(1003L, "Blue");
        colorSet.add(white);
        colorSet.add(black);
        colorSet.add(blue);

        Phone phone = new Phone();
        phone.setId(phoneId);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 40 Cesium");
        phone.setPrice(new BigDecimal("99.0"));
        phone.setColor(colorSet);

        return phone;
    }

    private Phone createAnotherPhoneWithPrice() {
        Set<Color> colorSet = new HashSet<>();
        Color white = new Color(1001L, "White");
        colorSet.add(white);

        Phone phone = new Phone();
        phone.setId(1006L);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 101 XS 2");
        phone.setPrice(new BigDecimal("270.0"));
        phone.setColor(colorSet);

        return phone;
    }
}
