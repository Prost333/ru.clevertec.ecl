package ru.clevertec.ecl.dao.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.entity.House;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class HouseRowMapper implements RowMapper<House> {


}
