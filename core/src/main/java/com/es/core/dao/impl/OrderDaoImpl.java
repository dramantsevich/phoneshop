package com.es.core.dao.impl;

import com.es.core.dao.OrderDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderDaoImpl implements OrderDao {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void update(int updateReserved, long id) {
        String sql = "update stocks \n" +
                "set reserved =:updateReserved" +
                "\n where phoneId=:id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("updateReserved", updateReserved)
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }
}
