package com.es.core.cart;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultCartService {
    @Autowired
    private JdbcPhoneDao phoneDao;

    protected Optional<CartItem> findCartItemForUpdate(Cart cart, Long phoneId, int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }

        List<CartItem> cartList = cart.getItems();
        Stock phone = phoneDao.getPhoneById(phoneId);
        CartItem cartItem = new CartItem(phone, quantity);

        return cartList.stream()
                .filter(c -> c.getStock().getPhone().getId().equals(cartItem.getStock().getPhone().getId()))
                .findAny();
    }

    protected void recalculateCartQuantity(Cart cart) {
        int totalQuantity = cart.getItems().stream()
                .map(CartItem::getQuantity).mapToInt(Integer::intValue).sum();

        cart.setTotalQuantity(totalQuantity);
    }

    protected void recalculateCartTotalCost(Cart cart) {
        BigDecimal totalCost = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getStock().getPhone().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalCost(totalCost);
    }
}
