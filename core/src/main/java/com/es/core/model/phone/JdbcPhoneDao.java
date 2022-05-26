package com.es.core.model.phone;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao{
    @Resource
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Phone> get(final Long key) {
        throw new UnsupportedOperationException("TODO");
    }

    public void save(final Phone phone) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query("Select p.*, c.id, c.code\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "offset " + offset + " limit" + limit, new PhoneMapper());
    }
}
