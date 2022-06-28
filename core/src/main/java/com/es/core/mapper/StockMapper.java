package com.es.core.mapper;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StockMapper implements RowMapper<Stock> {
    @Autowired
    private PhoneMapper phoneMapper;

    public Stock mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int phoneStock = Integer.parseInt(resultSet.getString("stock"));
        int reserved = Integer.parseInt(resultSet.getString("reserved"));

        Stock stock = new Stock();
        Phone phone = phoneMapper.mapRow(resultSet, rowNum);

        stock.setPhone(phone);
        stock.setStock(phoneStock);
        stock.setReserved(reserved);

        return stock;
    }
}
