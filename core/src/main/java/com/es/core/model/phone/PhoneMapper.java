package com.es.core.model.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.ColorMapper;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneMapper implements RowMapper<Phone> {
    public Phone mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Phone phone = (new BeanPropertyRowMapper<>(Phone.class).mapRow(resultSet, rowNum));
        Color color = (new ColorMapper().mapRow(resultSet, rowNum));

        phone.setColor(color);

        return phone;
    }
}
