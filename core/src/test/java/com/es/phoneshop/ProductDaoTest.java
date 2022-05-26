package com.es.phoneshop;

import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.PhoneDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration("classpath:/../../main/resources/context/applicationContext-core.xml")
public class ProductDaoTest {
    private PhoneDao phoneDao;

    @Before
    public void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        phoneDao = new JdbcPhoneDao();
        phoneDao.setDataSource(dataSource);
    }

    @Test
    public void testFindAll() {
        assertThat(phoneDao.findAll(0, 10)).isNotNull();
        assertThat(phoneDao.findAll(0, 10).size()).isPositive();
    }
}
