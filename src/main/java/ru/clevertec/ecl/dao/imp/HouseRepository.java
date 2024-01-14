package ru.clevertec.ecl.dao.imp;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dao.jdbc.HouseRowMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.House;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.service.PersonService;

@Repository
@AllArgsConstructor
@Transactional
public class HouseRepository implements HouseDao {

}
