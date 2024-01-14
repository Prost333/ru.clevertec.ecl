package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    PersonRes update(UUID uuid, PersonReq req);

    Person findById(Long id);

    PersonRes save(PersonReq personReq);
    PersonRes findByUUID (UUID id);

    Person findByUUIDInside (UUID id);
    List<PersonRes> findAll(int page, int pageSize);

    void delete(UUID uuid);
    List<PersonRes> findPersonWhoLivesInHouse(UUID houseId);
    List<PersonRes> findAbsolutText(String text);
}
