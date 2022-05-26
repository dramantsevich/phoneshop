package com.es.core.model.phone;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColorMapper {
    public Color mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Color color = new Color();
        color.setId(resultSet.getLong("id"));
        color.setCode(resultSet.getString("code"));

        return color;
    }
}
