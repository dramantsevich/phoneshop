package com.es.core.service.impl.cart;

import com.es.core.service.CartService;
import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhonePriceException;
import com.es.core.exception.QuantityNullException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Stock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HttpSessionCartServiceImpl implements CartService {
    @Autowired
    private StockDao stockDao;

    @Autowired
    private DefaultCartService defaultCartService;

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
    public void addPhone(Cart cart, Long phoneId, Long quantity) throws OutOfStockException {
        if (quantity == null) {
            throw new QuantityNullException();
        }

        Optional<CartItem> cartItemOptional = defaultCartService.findCartItemForUpdate(cart, phoneId, quantity.intValue());
        int productsAmount = cartItemOptional.map(CartItem::getQuantity).orElse(0);

        Stock phone = stockDao.getPhoneById(phoneId).orElseThrow(NullPointerException::new);
        if (phone.getPhone().getPrice() == null) {
            throw new PhonePriceException();
        }

        if ((phone.getStock() - phone.getReserved()) < productsAmount + quantity) {
            throw new OutOfStockException();
        }

        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(phone, quantity.intValue());

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity.intValue() + productsAmount);
        } else {
            cartList.add(cartItem);
        }

        defaultCartService.recalculateCartQuantity(cart);
        defaultCartService.recalculateCartTotalCost(cart);
    }

    @Override
    public void update(Cart cart, Long phoneId, String quantity) {
        if(StringUtils.isEmpty(quantity)) {
            throw new NumberFormatException();
        }

        Long quantityLong = Long.valueOf(quantity);

        if (quantityLong == null) {
            throw new QuantityNullException();
        }

        Stock phone = stockDao.getPhoneById(phoneId).orElseThrow(NullPointerException::new);

        if (phone.getPhone().getPrice() == null) {
            throw new PhonePriceException();
        }

        if ((phone.getStock() - phone.getReserved()) < quantityLong) {
            throw new OutOfStockException();
        }

        Optional<CartItem> cartItemOptional = defaultCartService.findCartItemForUpdate(cart, phoneId, quantityLong.intValue());
        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(phone, quantityLong.intValue());

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantityLong.intValue());
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
