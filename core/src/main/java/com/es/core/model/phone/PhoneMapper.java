package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class PhoneMapper implements RowMapper<Phone> {
    public Phone mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Phone phone = (new BeanPropertyRowMapper<>(Phone.class).mapRow(resultSet, rowNum));
        Set<Color> color = (new ColorMapper().mapRow(resultSet, rowNum));

        phone.setColor(color);

        return phone;
    }
}
