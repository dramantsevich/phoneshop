package com.es.phoneshop;

import com.es.core.model.phone.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        Phone phoneWithMultiColor = phoneList.get(6);
        Phone phone = createPhone();

        assertThat(Objects.equals(phoneWithMultiColor.getId(), phone.getId())).isTrue();
        assertThat(Objects.equals(phoneWithMultiColor.getBrand(), phone.getBrand())).isTrue();
        assertThat(Objects.equals(phoneWithMultiColor.getModel(), phone.getModel())).isTrue();
        assertThat(Objects.equals(phoneWithMultiColor.getPrice(), phone.getPrice())).isTrue();
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
}
