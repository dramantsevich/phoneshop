package com.es.phoneshop;

import com.es.core.model.phone.*;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "testContext.xml")
public class ProductDaoTest {
    @Autowired
    private JdbcPhoneDao phoneDao;

    @Test
    public void testFindWithLimit() {
        List<Phone> phoneList = phoneDao.findWithLimit(0, 10);

        assertThat(phoneList).isNotNull();
        assertThat(phoneList.size()).isEqualTo(10);
    }

    @Test
    public void testThatExpectedMultiColorsCodesContainsActualColorsCodes() {
        List<Phone> phoneList = phoneDao.findWithLimit(0, 10);
        Phone phoneWithMultiColor = phoneList.get(6);

        Set<Color> expectedColorSet = createColor();
        Set<Color> actualColorSet = phoneWithMultiColor.getColor();

        Set<String> expectedColorCode = expectedColorSet.stream()
                .map(Color::getCode)
                .collect(Collectors.toSet());

        Set<String> actualColorCode = actualColorSet.stream()
                .map(Color::getCode)
                .collect(Collectors.toSet());

        assertThat(expectedColorCode).containsAll(actualColorCode);
    }

    @Test
    public void testThatExpectedMultiColorsIdsContainsActualColorsIds() {
        List<Phone> phoneList = phoneDao.findWithLimit(0, 10);
        Phone phoneWithMultiColor = phoneList.get(6);

        Set<Color> expectedColorSet = createColor();
        Set<Color> actualColorSet = phoneWithMultiColor.getColor();

        Set<Long> expectedColorCode = expectedColorSet.stream()
                .map(Color::getId)
                .collect(Collectors.toSet());

        Set<Long> actualColorCode = actualColorSet.stream()
                .map(Color::getId)
                .collect(Collectors.toSet());

        assertThat(expectedColorCode).containsAll(actualColorCode);
    }

    @Test
    public void testThatActualPhoneHasImportantFields() {
        List<Phone> phoneList = phoneDao.findWithLimit(0, 10);
        Phone phoneWithMultiColor = phoneList.get(5);
        Phone phone = createPhone();

        assertThat(Objects.equals(phoneWithMultiColor.getId(), phone.getId())).isTrue();
        assertThat(Objects.equals(phoneWithMultiColor.getBrand(), phone.getBrand())).isTrue();
        assertThat(Objects.equals(phoneWithMultiColor.getModel(), phone.getModel())).isTrue();
        assertThat(Objects.equals(phoneWithMultiColor.getPrice(), phone.getPrice())).isTrue();
    }

    @Test
    public void testGetPhoneByIdWithStock() {
        Stock phone = phoneDao.getPhoneById(1011L);

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
        List<Stock> stockPhones = phoneDao.findAllWithStock();

        assertThat(stockPhones.size()).isEqualTo(3030);
    }

    @Test
    public void testFindByKeyword() {
        int pageNo = 2;
        int size = 10;
        String keyword = "Samsung";

        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        Page<Stock> stockPage = phoneDao.findByKeyword(firstPage, keyword);

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
        Page<Stock> stockPage = phoneDao.findSortedPhonesByKeyword(firstPage, property, direction, keyword);

        assertThat(stockPage.stream().allMatch(p -> p.getPhone().getModel()
                .contains(keyword))).isTrue();
    }

    private Set<Color> createColor() {
        Color black = new Color(1000L, "Black");
        Color yellow = new Color(1002L, "Yellow");
        Color blue = new Color(1003L, "Blue");

        Set<Color> colorSet = new HashSet<>();
        colorSet.add(black);
        colorSet.add(yellow);
        colorSet.add(blue);

        return colorSet;
    }

    private Phone createPhone() {
        Phone phone = new Phone();
        phone.setId(1011L);
        phone.setBrand("ARCHOS");
        phone.setModel("ARCHOS 40 Cesium");
        phone.setPrice(new BigDecimal("99.0"));

        return phone;
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
        return phoneDao.findAll(firstPage);
    }
}
