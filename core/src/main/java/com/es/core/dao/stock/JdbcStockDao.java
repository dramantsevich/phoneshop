package com.es.core.dao.stock;

import com.es.core.mapper.StockMapper;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StockMapper stockMapper;

    public Optional<Stock> getPhoneById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.id =" + id + "\n" +
                "group by p.id", stockMapper));
    }

    @Override
    public List<Stock> findAllWithStock(String property) {
        String withPrice = "\n";

        if (Objects.equals(property, "price")) {
            withPrice = "and p.price > 0\n";
        }

        return jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0\n" + withPrice +
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

        long count = findAllWithStock("").size();

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

        long count = getByKeywordSizePhonesList(keyword, "");

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findSortedPhonesByKeyword(Pageable pageable, String property, String direction, String keyword) {
        String withPrice = "\n";

        if (Objects.equals(property, "price")) {
            withPrice = "and p.price > 0\n";
        }

        List<Stock> phoneList = jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.model like '%" + keyword + "%' \n" + withPrice +
                "group by p.id\n" +
                "order by p." + property + " " + direction + "\n" +
                " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(), stockMapper);

        long count = getByKeywordSizePhonesList(keyword, property);

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findSortedPhones(Pageable pageable, String property, String direction) {
        String withPrice = "\n";

        if (Objects.equals(property, "price")) {
            withPrice = "and p.price > 0\n";
        }

        List<Stock> phoneList = jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0" + withPrice +
                "group by p.id\n" +
                "order by p." + property + " " + direction +
                " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(), stockMapper);

        long count = findAllWithStock(property).size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    private long getByKeywordSizePhonesList(String keyword, String property) {
        String withPrice = "\n";

        if (Objects.equals(property, "price")) {
            withPrice = "and p.price > 0\n";
        }

        return jdbcTemplate.query("select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
                "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
                "from phones p\n" +
                "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
                "inner join colors c on p2c.colorId = c.Id\n" +
                "inner join stocks s on p.id = s.phoneId\n" +
                "where s.stock > 0 and p.model like '%" + keyword + "%' \n" + withPrice +
                "group by p.id\n" +
                "order by p.id asc", stockMapper).size();
    }
}
