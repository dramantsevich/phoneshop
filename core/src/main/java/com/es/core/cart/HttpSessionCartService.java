package com.es.core.cart;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    @Autowired
    private JdbcPhoneDao phoneDao;

    @Autowired
    private DefaultCartService defaultCartService;

    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";

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
        Stock phone = phoneDao.getPhoneById(phoneId);

        if (phone.getStock() < quantity) {
            throw new OutOfStockException(phone, quantity.intValue(), phone.getStock());
        }

        Optional<CartItem> cartItemOptional = defaultCartService.findCartItemForUpdate(cart, phoneId, quantity.intValue());
        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(phone, quantity.intValue());

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity.intValue());
        } else {
            cartList.add(cartItem);
        }

        //these methods are used in further development

//        defaultCartService.recalculateCartQuantity(cart);
//        defaultCartService.recalculateCartTotalCost(cart);
    }

    @Override
    public void update(Map<Long, Long> items) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void remove(Long phoneId) {
        throw new UnsupportedOperationException("TODO");
    }
}
