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
    private final SessionFactory sessionFactory;
    private final PersonMapper personMapper;
    private final JdbcTemplate jdbcTemplate;
    private final PersonRowMapper personRowMapper;

    @Override
    public void delete(UUID uuid) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> cr = cb.createQuery(Person.class);
            Root<Person> root = cr.from(Person.class);
            cr.select(root).where(cb.equal(root.get("uuid"), uuid));

            Query<Person> query = session.createQuery(cr);
            Person person = query.uniqueResult();

            person.getOwnedHouses().clear();
            if (person.getHouse() != null) {
                person.getHouse().getResidents().remove(person);
            }

            session.remove(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public PersonRes update(Person o) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(o);
            transaction.commit();
            return personMapper.toResponse(o);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Person> findAll(int page, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Person> query = session.createQuery("from Person", Person.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Person findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return (session.get(Person.class, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Person findByUUID(UUID uuid) {
        PersonRes personRes = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> cr = cb.createQuery(Person.class);
            Root<Person> root = cr.from(Person.class);
            cr.select(root).where(cb.equal(root.get("uuid"), uuid));

            Query<Person> query = session.createQuery(cr);
            Person person = query.uniqueResult();

            if (person != null) {
                return person;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Person person) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            person.setCreateDate(LocalDateTime.now());
            person.setUpdateDate(person.getCreateDate());
            person.setUuid(UUID.randomUUID());
            session.save(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Person> findPersonWhoLivesInHouse(UUID houseId) {

        return jdbcTemplate.query("""
                SELECT p.* FROM persons p
                JOIN houses h ON p.house_id = h.id
                WHERE h.uuid = ?
                """, personRowMapper, houseId);
    }
    @Override
    public List<Person> findAbsolutText(String text) {

        return jdbcTemplate.query("""            
            SELECT * FROM persons
               WHERE CONCAT(name, ' ', surname)
               LIKE '%' || ? || '%'
            """, personRowMapper,text);
    }


}
