package com.es.phoneshop.unit;

import com.es.core.dao.StockDao;
import com.es.core.exception.NegativeQuantityException;
import com.es.core.exception.PhonePriceException;
import com.es.core.exception.QuantityNullException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.impl.cart.DefaultCartService;
import lombok.SneakyThrows;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCartServiceTest {
    private final Long phoneId = 1011L;
    private final StockDao mockStockDao = mock(StockDao.class);
    private final DefaultCartService defaultCartService = new DefaultCartService(mockStockDao);

    @SneakyThrows
    @Test
    public void testFindCartItemForUpdate() {
        Cart cart = createNotEmptyCart(createPhoneWithPrice(), 2L);

        doReturn(createOptionalStock(createPhoneWithPrice())).when(mockStockDao).getPhoneById(phoneId);
        Optional<CartItem> cartItemOptional = defaultCartService.findCartItemForUpdate(cart, phoneId, 4L);

        assertThat(cartItemOptional).isPresent();
    }

    @SneakyThrows
    @Test
    public void testCartTotalCostCalculationWorksCorrectly() {
        Cart cart = createNotEmptyCart(createPhoneWithPrice(), 2L);
        defaultCartService.recalculateCartTotalCost(cart);

        assertThat(cart.getTotalCost()).isEqualTo(BigDecimal.valueOf(198.0));
    }

    @SneakyThrows
    @Test
    public void testCartTotalQuantityCalculationWorksCorrectly() {
        Cart cart = createNotEmptyCart(createPhoneWithPrice(), 2L);
        defaultCartService.recalculateCartQuantity(cart);

        assertThat(cart.getTotalQuantity()).isEqualTo(2);
    }

    @SneakyThrows
    @Test
    public void testGetPhone() {
        doReturn(createOptionalStock(createPhoneWithPrice())).when(mockStockDao).getPhoneById(phoneId);
        Stock stock = defaultCartService.getPhone(phoneId);

        assertThat(stock).isNotNull();
        assertThat(stock.getPhone().getId()).isEqualTo(phoneId);
    }

    @SneakyThrows
    @Test
    public void testGetPhoneThrowPhonePriceException() {
        doReturn(createOptionalStock(createPhoneWithoutPrice())).when(mockStockDao).getPhoneById(phoneId);

        assertThatThrownBy(() -> {
            defaultCartService.getPhone(phoneId);
        }).isInstanceOf(PhonePriceException.class);
    }

    @SneakyThrows
    @Test
    public void testGetPhoneThrowQuantityNullException() {
        doReturn(createOptionalStock(createPhoneWithPrice())).when(mockStockDao).getPhoneById(phoneId);

        assertThatThrownBy(() -> {
            defaultCartService.getPhone(phoneId);
        }).isInstanceOf(QuantityNullException.class);
    }

    @SneakyThrows
    @Test
    public void testGetPhoneThrowNegativeQuantityException() {
        doReturn(createOptionalStock(createPhoneWithPrice())).when(mockStockDao).getPhoneById(phoneId);

        assertThatThrownBy(() -> {
            defaultCartService.getPhone(phoneId);
        }).isInstanceOf(NegativeQuantityException.class);
    }

    private Optional<Stock> createOptionalStock(Phone phone) {
        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(11);
        stock.setReserved(0);

        return Optional.of(stock);
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

    private Phone createPhoneWithPrice() {
        Phone phone = new Phone();
        phone.setId(phoneId);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 40 Cesium");
        phone.setPrice(new BigDecimal("99.0"));

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
