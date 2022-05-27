package com.es.core.model.phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ColorMapper {
    public Set<Color> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Set<Color> colorSet = new HashSet<>();

        long[] colorIds = parseColorId(resultSet.getString("colorId"));
        String[] colorCodes = parseColorCode(resultSet.getString("colorCode"));

        for(int i = 0; i < colorIds.length; i++){
            Color color = new Color();

            color.setId(colorIds[i]);
            color.setCode(colorCodes[i]);

            colorSet.add(color);
        }

        return colorSet;
    }

    private long[] parseColorId(String columnLabel) {
        return Arrays.stream(columnLabel.split(",")).mapToLong(Long::parseLong).toArray();
    }

    private String[] parseColorCode(String columnLabel) {
        return columnLabel.split(",");
    }
}
