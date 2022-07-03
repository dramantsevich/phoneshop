package com.es.core.service.impl.cart;

import com.es.core.exception.*;
import com.es.core.service.CartService;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HttpSessionCartServiceImpl implements CartService {
    private final DefaultCartService defaultCartService;

    @Autowired
    public HttpSessionCartServiceImpl(DefaultCartService defaultCartService) {
        this.defaultCartService = defaultCartService;
    }

    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartServiceImpl.class.getName() + ".cart";

    @Override
    public Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);

        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }

        return cart;
    }

    @Override
    public void addPhone(Cart cart, Long phoneId, Long quantity) {
        Optional<CartItem> cartItemOptional = defaultCartService.findCartItemForUpdate(cart, phoneId, quantity);
        Long productsAmount = cartItemOptional.map(CartItem::getQuantity).orElse(0L);

        Stock phone = defaultCartService.getPhone(phoneId);

        int colorSize = phone.getPhone().getColor().size();
        if ((phone.getStock() / colorSize - phone.getReserved() / colorSize) < productsAmount + quantity) {
            throw new OutOfStockException();
        }

        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(phone, quantity);

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity + productsAmount);
        } else {
            cartList.add(cartItem);
        }

        defaultCartService.recalculateCartQuantity(cart);
        defaultCartService.recalculateCartTotalCost(cart);
    }

    @Override
    public void update(Cart cart, Long phoneId, Long quantity) {

        Stock phone = defaultCartService.getPhone(phoneId);

        int colorSize = phone.getPhone().getColor().size();
        if ((phone.getStock() / colorSize - phone.getReserved() / colorSize) < quantity) {
            throw new OutOfStockException();
        }

        Optional<CartItem> cartItemOptional = defaultCartService.findCartItemForUpdate(cart, phoneId, quantity);
        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(phone, quantity);

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity);
        } else {
            cartList.add(cartItem);
        }

        defaultCartService.recalculateCartQuantity(cart);
        defaultCartService.recalculateCartTotalCost(cart);
    }

    @Override
    public void remove(Cart cart, Long phoneId) {
        cart.getItems().removeIf(item -> item.getStock().getPhone().getId().equals(phoneId));

        defaultCartService.recalculateCartQuantity(cart);
        defaultCartService.recalculateCartTotalCost(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        List<CartItem> cartList = cart.getItems();

        cartList.clear();
        cart.setTotalCost(new BigDecimal(0));
        cart.setTotalQuantity(0);
    }
}
