package com.es.phoneshop;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "testContext.xml")
public class StockDaoTest {
    @Autowired
    private StockDao stockDao;

    @Test
    public void testGetPhoneByIdWithStock() {
        Stock phone = stockDao.getPhoneById(1011L);

        assertThat(phone.getStock()).isGreaterThan(0);
        assertThat(phone.getPhone().getId()).isEqualTo(1011L);
    }

    @Test
    public void testFindAll() {
        Page<Stock> stockPage = createPageWithStockPhones();
        Stock phoneWithStock = createPhoneWithStock();

        assertThat(stockPage.stream().anyMatch(p -> p.getPhone().getId()
                .equals(phoneWithStock.getPhone().getId()))).isTrue();
    }

    @Test
    public void testFindAllWithStock() {
        List<Stock> stockPhones = stockDao.findAllWithStock("");

        assertThat(stockPhones.size()).isEqualTo(3030);
    }

    @Test
    public void testFindByKeyword() {
        int pageNo = 2;
        int size = 10;
        String keyword = "Samsung";

        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        Page<Stock> stockPage = stockDao.findByKeyword(firstPage, keyword);

        assertThat(stockPage.stream().allMatch(p -> p.getPhone().getModel()
                .contains(keyword))).isTrue();
    }

    @Test
    public void testFindSortedPhonesByKeyword() {
        int pageNo = 2;
        int size = 10;
        String keyword = "Samsung";
        String property = "displaysizeinches";
        String direction = "desc";

        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        Page<Stock> stockPage = stockDao.findSortedPhonesByKeyword(firstPage, property, direction, keyword);

        assertThat(stockPage.stream().allMatch(p -> p.getPhone().getModel()
                .contains(keyword))).isTrue();
    }

    private Stock createPhoneWithStock() {
        Set<Color> colorSet = new HashSet<>();
        Color white = new Color(1001L, "White");
        Color black = new Color(1000L, "Black");
        colorSet.add(white);
        colorSet.add(black);

        Phone phone = new Phone();
        phone.setId(1413L);
        phone.setBrand("Alcatel");
        phone.setModel("Alcatel OneTouch Idol X+");
        phone.setPrice(new BigDecimal("330.0"));
        phone.setColor(colorSet);

        Stock phoneWithStock = new Stock();
        phoneWithStock.setPhone(phone);
        phoneWithStock.setStock(6);
        phoneWithStock.setReserved(6);

        return phoneWithStock;
    }

    private Page<Stock> createPageWithStockPhones() {
        int pageNo = 23;
        int size = 10;

        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        return stockDao.findAll(firstPage);
    }
}
