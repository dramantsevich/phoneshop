package com.es.core.model.phone;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ColorMapper {
    public Set<Color> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        long[] colorIds = parseColorId(resultSet.getString("colorId"));
        String[] colorCodes = parseColorCode(resultSet.getString("colorCode"));

        return IntStream.range(0, colorIds.length)
                .mapToObj(i -> new Color(colorIds[i], colorCodes[i]))
                .collect(Collectors.toSet());
    }

    private long[] parseColorId(String columnLabel) {
        return Arrays.stream(columnLabel.split(",")).mapToLong(Long::parseLong).toArray();
    }

    private String[] parseColorCode(String columnLabel) {
        return columnLabel.split(",");
    }
}
