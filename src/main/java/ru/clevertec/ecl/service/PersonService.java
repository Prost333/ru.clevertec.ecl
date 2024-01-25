package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    PersonRes update(UUID uuid, PersonReq req);

    PersonRes save(PersonReq personReq);

    Person saveEntity(Person person);

    PersonRes findByUUID(UUID uuid);

    Person findByUUIDInside(UUID id);

    Page<PersonRes> findAll(int page, int pageSize);

    Page<PersonRes> findByPersonByHistory(UUID uuid, PersonType type, Pageable pageable);

    void delete(UUID uuid);

    List<PersonRes> findPersonWhoLivesInHouse(UUID houseId);

    List<PersonRes> findAbsolutText(String text);
}
