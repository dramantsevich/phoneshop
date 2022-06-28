package com.es.core.dao.impl;

import com.es.core.dao.StockDao;
import com.es.core.mapper.StockMapper;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JdbcStockDao implements StockDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StockMapper stockMapper;

    @Autowired
    public JdbcStockDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, StockMapper stockMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.stockMapper = stockMapper;
    }

    private static final String QUERY = "select sum(s.stock) as stock, sum(s.reserved) as reserved,\n" +
            "group_concat(c.id) as colorId, group_concat(c.code) as colorCode, p.*\n" +
            "from phones p\n" +
            "inner join phone2color p2c on p.Id = p2c.phoneId\n" +
            "inner join colors c on p2c.colorId = c.Id\n" +
            "inner join stocks s on p.id = s.phoneId\n" +
            "where s.stock > 0\n";


    @Override
    public Optional<Stock> getPhoneById(Long id) {
        String sql = QUERY + " and p.id = :id\n" +
                "group by p.id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, parameter, stockMapper));
    }

    @Override
    public Page<Stock> findAll(Pageable pageable) {
        String sql = QUERY +
                "group by p.id\n" +
                "order by p.id asc \n" +
                "LIMIT :limit OFFSET :offset";
        SqlParameterSource parameter = setLimitOffsetParameters(pageable);

        List<Stock> phoneList = namedParameterJdbcTemplate.query(sql, parameter, stockMapper);

        long count = getAllWithStock("").size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findByKeyword(Pageable pageable, String keyword) {
        String sql = QUERY + " and p.model like '%" + keyword + "%' \n" +
                "group by p.id\n" +
                "order by p.id asc" +
                " LIMIT :limit OFFSET :offset";
        SqlParameterSource parameter = setLimitOffsetParameters(pageable);

        List<Stock> phoneList = namedParameterJdbcTemplate.query(sql, parameter, stockMapper);

        long count = getByKeywordSizePhonesList(keyword, "").size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findSortedPhonesByKeyword(Pageable pageable, String property, String direction, String keyword) {
        String withPrice = setQueryWithPrice(property);
        String sql = QUERY + " and p.model like '%" + keyword + "%' \n" + withPrice +
                "group by p.id\n" +
                "order by p." + property + " " + direction + "\n" +
                " LIMIT :limit OFFSET :offset";
        SqlParameterSource parameter = setLimitOffsetParameters(pageable);

        List<Stock> phoneList = namedParameterJdbcTemplate.query(sql, parameter, stockMapper);

        long count = getByKeywordSizePhonesList(keyword, property).size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    @Override
    public Page<Stock> findSortedPhones(Pageable pageable, String property, String direction) {
        String withPrice = setQueryWithPrice(property);
        String sql = QUERY + withPrice +
                "group by p.id\n" +
                "order by p." + property + " " + direction +
                " LIMIT :limit OFFSET :offset";
        SqlParameterSource parameter = setLimitOffsetParameters(pageable);

        List<Stock> phoneList = namedParameterJdbcTemplate.query(sql, parameter, stockMapper);

        long count = getAllWithStock(property).size();

        return new PageImpl<>(phoneList, pageable, count);
    }

    private SqlParameterSource setLimitOffsetParameters(Pageable pageable) {
        return new MapSqlParameterSource()
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
    }

    private List<Stock> getAllWithStock(String property) {
        String withPrice = setQueryWithPrice(property);
        String sql = QUERY + withPrice +
                "group by p.id\n" +
                "order by p.id asc\n";

        return namedParameterJdbcTemplate.query(sql, stockMapper);
    }

    private List<Stock> getByKeywordSizePhonesList(String keyword, String property) {
        String withPrice = setQueryWithPrice(property);
        String sql = QUERY + " and p.model like '%" + keyword + "%' \n" + withPrice +
                "group by p.id\n" +
                "order by p.id asc";

        return namedParameterJdbcTemplate.query(sql, stockMapper);
    }

    private String setQueryWithPrice(String property) {
        String withPrice = "";

        if (Objects.equals(property, "price")) {
            withPrice = "and p.price > 0\n";
        }

        return withPrice;
    }
}
