package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonDao{

    PersonRes update(Person o);

    Person findById(Long id);

    void save(Person person);
    Person findByUUID (UUID id);
    List<Person> findAll(int page, int pageSize);

    void delete(UUID uuid);

    List<Person> findPersonWhoLivesInHouse(UUID houseId);
    List<Person> findAbsolutText(String text);

}
