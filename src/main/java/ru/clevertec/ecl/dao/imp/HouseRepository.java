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
import ru.clevertec.ecl.entity.House;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


@Repository
@AllArgsConstructor
@Transactional
public class HouseRepository implements HouseDao {
    private final SessionFactory sessionFactory;
    private final JdbcTemplate jdbcTemplate;
    private final HouseRowMapper houseRowMapper;
    private final HouseMapper houseMapper;


    @Override
    public void save(House house) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(house);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public House findByUUID(UUID id) {
        House house = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<House> cr = cb.createQuery(House.class);
            Root<House> root = cr.from(House.class);
            cr.select(root).where(cb.equal(root.get("uuid"), id));

            Query<House> query = session.createQuery(cr);
            House house1 = query.uniqueResult();

            if (house1 != null) {
                return house1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<House> cr = cb.createQuery(House.class);
            Root<House> root = cr.from(House.class);
            cr.select(root).where(cb.equal(root.get("uuid"), uuid));

            Query<House> query = session.createQuery(cr);
            House house = query.uniqueResult();

            house.getResidents().clear();
            house.getOwners().clear();

            session.remove(house);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public HouseRes update(House o) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(o);
            transaction.commit();
            return houseMapper.toResponse(o);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HouseRes> findAll(int page, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<House> query = session.createQuery("from House", House.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list().stream().map(houseMapper::toResponse).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HouseRes findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return houseMapper.toResponse(session.get(House.class, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<House> findOwners(UUID houseId) {

        return jdbcTemplate.query("""
                SELECT * FROM houses h 
                JOIN houses_persons hp ON h.id = hp.houses_id 
                JOIN persons p ON hp.persons_id = p.id WHERE p.uuid = ?
                """, houseRowMapper, houseId);
    }

    @Override
    public List<House> findAbsolutText(String text) {

        return jdbcTemplate.query("""            
                SELECT * FROM houses
                WHERE CONCAT(area, ' ', country, ' ', city, ' ', street) 
                LIKE '%' || ? || '%'
                """, houseRowMapper, text);
    }
}
