package com.es.core.service.impl.cart;

import com.es.core.dao.StockDao;
import com.es.core.exception.*;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultCartService {
    private final StockDao stockDao;

    @Autowired
    public DefaultCartService(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    public Optional<CartItem> findCartItemForUpdate(Cart cart, Long phoneId, Long quantity) throws OutOfStockException {
        List<CartItem> cartList = cart.getItems();
        Stock phone = stockDao.getPhoneById(phoneId).orElseThrow(PhoneNotFoundException::new);

        return cartList.stream()
                .filter(c -> c.getStock().getPhone().getId()
                        .equals(new CartItem(phone, quantity).getStock().getPhone().getId()))
                .findAny();
    }

    public void recalculateCartQuantity(Cart cart) {
        long totalQuantity = cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToLong(Long::longValue)
                .sum();

        cart.setTotalQuantity((int) totalQuantity);
    }

    public void recalculateCartTotalCost(Cart cart) {
        BigDecimal totalCost = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getStock().getPhone().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalCost(totalCost);
    }

    public Stock getPhone(Long phoneId) {
        Stock phone = stockDao.getPhoneById(phoneId).orElseThrow(PhoneNotFoundException::new);

        if (phone.getPhone().getPrice() == null) {
            throw new PhonePriceException();
        }

        return phone;
    }
}
