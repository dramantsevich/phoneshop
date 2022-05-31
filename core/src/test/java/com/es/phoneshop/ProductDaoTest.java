package com.es.phoneshop;

import com.es.core.model.phone.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "testContext.xml")
public class ProductDaoTest {
    @Autowired
    private JdbcPhoneDao phoneDao;

    @Test
    public void testFindWithLimit() {
        List<Phone> phoneList = phoneDao.findWithLimit(0, 10);
        Phone firstPhone = phoneList.get(0);

        assertThat(phoneList).isNotNull();
        assertThat(firstPhone.getColor()).isNotNull();
        assertThat(phoneList.stream().findFirst()).isPresent();
        assertThat(phoneList.size()).isEqualTo(10);
    }
}
