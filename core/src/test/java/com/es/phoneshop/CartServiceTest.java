package com.es.phoneshop;

import com.es.core.exception.*;
import com.es.core.service.CartService;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "testContext.xml")
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    public void testCartUpdateWorksCorrectly() {
        Phone phone = createPhoneWithPrice();
        Cart cart = createCart(phone);
        Long id = cart.getItems().get(0).getStock().getPhone().getId();
        String quantityToUpdate = "4L";

        cartService.update(cart, id, quantityToUpdate);

        assertThat(cart.getTotalQuantity()).isEqualTo(Integer.valueOf(quantityToUpdate));
    }

    @Test
    public void testCartUpdateThrowQuantityNullException() {
        Phone phone = createPhoneWithPrice();
        Cart cart = createCart(phone);
        Long id = cart.getItems().get(0).getStock().getPhone().getId();

        assertThatThrownBy(() -> {
            cartService.update(cart, id, null);
        }).isInstanceOf(QuantityNullException.class);
    }

    @Test
    public void testCartUpdateThrowPhonePriceException() {
        Phone phone = createPhoneWithoutPrice();
        Cart cart = createCart(phone);
        Long id = cart.getItems().get(0).getStock().getPhone().getId();

        assertThatThrownBy(() -> {
            cartService.update(cart, id, "4L");
        }).isInstanceOf(PhonePriceException.class);
    }

    @Test
    public void testCartUpdateThrowOutOfStockException() {
        Phone phone = createPhoneWithPrice();
        Cart cart = createCart(phone);
        Long id = cart.getItems().get(0).getStock().getPhone().getId();

        assertThatThrownBy(() -> {
            cartService.update(cart, id, "100L");
        }).isInstanceOf(OutOfStockException.class);
    }

    @Test
    public void testCartUpdateThrowNegativeQuantityException() {
        Phone phone = createPhoneWithPrice();
        Cart cart = createCart(phone);
        Long id = cart.getItems().get(0).getStock().getPhone().getId();

        assertThatThrownBy(() -> {
            cartService.update(cart, id, "-100L");
        }).isInstanceOf(NegativeQuantityException.class);
    }

    @Test
    public void testCartRemove() {
        Phone phone = createPhoneWithPrice();
        Cart cart = createCart(phone);
        Long id = cart.getItems().get(0).getStock().getPhone().getId();

        cartService.remove(cart, id);

        assertThat(cart.getTotalQuantity()).isZero();
        assertThat(cart.getTotalCost()).isZero();
    }

    @Test
    public void testСlearCart() {
        Phone phone = createPhoneWithPrice();
        Cart cart = createCart(phone);

        CartItem cartItem = createCartItem(createPhoneWithoutPrice()).get(0);
        cart.getItems().add(cartItem);

        assertThat(cart.getItems().size()).isEqualTo(2);

        cartService.clearCart(cart);

        assertThat(cart.getTotalQuantity()).isZero();
        assertThat(cart.getTotalCost()).isZero();
    }

    private Cart createCart(Phone phone) {
        Cart cart = new Cart();
        cart.setItems(createCartItem(phone));
        cart.setTotalQuantity(2);
        try {
            cart.setTotalCost(phone.getPrice().multiply(BigDecimal.valueOf(cart.getTotalQuantity())));
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

        cartItemList.add(new CartItem(stock, 2));

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

    private Phone createPhoneWithoutPrice() {
        Phone phone = new Phone();
        phone.setId(1002L);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 101 Internet Tablet");
        phone.setPrice(null);

        return phone;
    }
}
