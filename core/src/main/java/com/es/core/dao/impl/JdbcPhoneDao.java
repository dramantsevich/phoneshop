package com.es.core.dao.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.mapper.PhoneMapper;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PhoneMapper phoneMapper;

    public Optional<Phone> get(final Long key) {
        throw new UnsupportedOperationException("TODO");
    }

    public void save(final Phone phone) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<Phone> findWithLimit(int offset, int limit) {
        return jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved, " +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.* \n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0\n" +
                "group by p.id\n" +
                "order by p.id asc \n" +
                "offset " + offset + " limit " + limit, phoneMapper);
    }
}
