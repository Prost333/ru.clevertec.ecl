package ru.clevertec.ecl.dao.imp;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dao.jdbc.PersonRowMapper;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
@AllArgsConstructor
@Transactional
public class PersonRepository implements PersonDao {

}
