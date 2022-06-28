package com.es.core.mapper;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class PhoneMapper implements RowMapper<Phone> {
    @Autowired
    private ColorMapper colorMapper;

    public Phone mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Phone phone = (BeanPropertyRowMapper.newInstance(Phone.class).mapRow(resultSet, rowNum));
        Set<Color> color = (colorMapper.mapRow(resultSet, rowNum));

        phone.setColor(color);

        return phone;
    }
}
