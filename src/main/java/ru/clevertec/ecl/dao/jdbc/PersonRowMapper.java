package ru.clevertec.ecl.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.enim.Sex;
import ru.clevertec.ecl.entity.Passport;
import ru.clevertec.ecl.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class PersonRowMapper implements RowMapper<Person> {



}