package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao extends JpaRepository<Person, UUID> {

    @Query("SELECT p FROM Person p WHERE p.uuid = :uuid")
    Optional<Person> findPerson(@Param("uuid")UUID uuid);

    @Query("SELECT p FROM Person p WHERE p.house.uuid = :houseId")
    List<Person> findPersonWhoLivesInHouse(@Param("houseId") UUID houseId);

    @Query("SELECT p FROM Person p WHERE p.name LIKE %:text% OR p.surname LIKE %:text%")
    List<Person> findAbsolutText(String text);

    Page<Person> findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(UUID uuid, PersonType type, Pageable pageable);

    Page<Person> findAll(Pageable pageable);
}
