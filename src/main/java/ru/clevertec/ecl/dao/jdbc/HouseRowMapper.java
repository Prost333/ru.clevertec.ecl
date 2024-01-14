package ru.clevertec.ecl.dao.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.entity.House;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class HouseRowMapper implements RowMapper<House> {

    @Override
    public House mapRow(ResultSet rs, int rowNum) throws SQLException {

        House house = new House();

        house.setId(rs.getLong("id"));
        house.setUuid((UUID) rs.getObject("uuid"));
        house.setArea(rs.getString("area"));
        house.setCountry(rs.getString("country"));
        house.setCity(rs.getString("city"));
        house.setStreet(rs.getString("street"));
        house.setNumber(rs.getInt("number"));
        house.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());

        return house;
    }
}
