package com.es.core.dao.impl;

import com.es.core.dao.OrderDao;
import com.es.core.exception.NoDataFoundBySQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderDaoImpl implements OrderDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public OrderDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void updateReservedValueById(int updateReserved, long id) {
        String sql = "update stocks \n" +
                "set reserved =:updateReserved" +
                "\n where phoneId=:id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("updateReserved", updateReserved)
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }

    @Override
    public int getReservedValueById(long id) {
        String sql = "select reserved " +
                "from stocks " +
                "where phoneid=:id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);

        return Optional.ofNullable(namedParameterJdbcTemplate
                        .queryForObject(sql, parameter, Integer.class))
                .orElseThrow(NoDataFoundBySQLException::new);
    }

    @Override
    public int getStockValueById(long id) {
        String sql = "select stock " +
                "from stocks " +
                "where phoneid=:id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);

        return Optional.ofNullable(namedParameterJdbcTemplate
                        .queryForObject(sql, parameter, Integer.class))
                .orElseThrow(NoDataFoundBySQLException::new);
    }
}
