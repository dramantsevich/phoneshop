package com.es.core.dao.cart;

import com.es.core.dao.stock.StockDao;
import com.es.core.exception.NegativeQuantityException;
import com.es.core.exception.OutOfStockException;
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
    @Autowired
    private StockDao stockDao;

    protected Optional<CartItem> findCartItemForUpdate(Cart cart, Long phoneId, int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new NegativeQuantityException();
        }

        List<CartItem> cartList = cart.getItems();
        Stock phone = stockDao.getPhoneById(phoneId).orElseThrow(NullPointerException::new);
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
