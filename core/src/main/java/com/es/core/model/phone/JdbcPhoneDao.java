package com.es.core.model.phone;

import com.es.core.mapper.PhoneMapper;
import com.es.core.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private StockMapper stockMapper;

    public Optional<Phone> get(final Long key) {
        throw new UnsupportedOperationException("TODO");
    }

    public Stock getPhoneById(Long id) {
        return jdbcTemplate.queryForObject("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.id =" + id + "\n" +
                "group by p.id", stockMapper);
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

    @Override
    public List<Stock> findAllWithStock() {
        return jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0\n" +
                "group by p.id\n" +
                "order by p.id asc\n", stockMapper);
    }

    @Override
    public Page<Stock> findAll(Pageable pageable) {
        List<Stock> phoneList = jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0\n" +
                "group by p.id\n" +
                "order by p.id asc \n" +
                " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(), stockMapper);

        long count = findAllWithStock().size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findByKeyword(Pageable pageable, String keyword) {
        List<Stock> phoneList = jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.model like '%" + keyword + "%' \n" +
                "group by p.id\n" +
                "order by p.id asc" +
                " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(), stockMapper);

        long count = getByKeywordSizePhonesList(keyword);

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findSortedPhonesByKeyword(Pageable pageable, String property, String direction, String keyword) {
        List<Stock> phoneList = jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.model like '%" + keyword + "%' \n" +
                "group by p.id\n" +
                "order by p." + property + " " + direction + "\n" +
                " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(), stockMapper);

        long count = getByKeywordSizePhonesList(keyword);

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findSortedPhones(Pageable pageable, String property, String direction) {
        List<Stock> phoneList = jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0\n" +
                "group by p.id\n" +
                "order by p." + property + " " + direction + "\n" +
                " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(), stockMapper);

        long count = findAllWithStock().size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    private long getByKeywordSizePhonesList(String keyword) {
        return jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.model like '%" + keyword + "%' \n" +
                "group by p.id\n" +
                "order by p.id asc", stockMapper).size();
    }
}
