package ru.clevertec.ecl.service.imp;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;
import ru.clevertec.ecl.exeption.EntityNotFoundException;
import ru.clevertec.ecl.proxy.Cache;
import ru.clevertec.ecl.service.PersonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonServiceImp implements PersonService {
    private final PersonDao personDao;

    private final PersonMapper personMapper;

    @Override
    @Cache
    public PersonRes update(UUID uuid, PersonReq req) {
        Person person = personDao.findPerson(uuid).orElseThrow(() -> new EntityNotFoundException("User not found"));
        person.setName(req.getName());
        person.setSurname(req.getSurname());
        person.setSex(req.getSex());
        person.setPassport(req.getPassport());
        person.setUpdateDate(LocalDateTime.now());
        personDao.save(person);
        return personMapper.toResponse(person);
    }


    @Override
    @Cache
    public PersonRes save(PersonReq personReq) throws EntityNotFoundException {
        Person person = personMapper.toRequest(personReq);
        personDao.save(person);
        return personMapper.toResponse(person);
    }

    @Override
    public Person saveEntity(Person person) {
        return personDao.save(person);
    }

    @Override
    @Cache
    public PersonRes findByUUID(UUID uuid) {
        Person person = personDao.findPerson(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return personMapper.toResponse(person);

    }

    @Override
    public Person findByUUIDInside(UUID id) {
        return personDao.findPerson(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public Page<PersonRes> findAll(int page, int pageSize) {
        Page<PersonRes> personsPage = personDao.findAll(PageRequest.of(page, pageSize))
                .map(personMapper::toResponse);
        if (personsPage.isEmpty()) {
            throw new EntityNotFoundException("No persons found");
        }
        return personsPage;
    }

    @Override
    public Page<PersonRes> findByPersonByHistory(UUID uuid, PersonType type, Pageable pageable) {
        return personDao.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(uuid, type, pageable)
                .map(personMapper::toResponse);
    }

    @Override
    @Cache
    public void delete(UUID id) {
        try {
            personDao.delete(findByUUIDInside(id));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User not Found");
        }

    }

    @Override
    public List<PersonRes> findPersonWhoLivesInHouse(UUID houseId) throws EntityNotFoundException {

        return personDao.findPersonWhoLivesInHouse(houseId).
                stream().map(personMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonRes> findAbsolutText(String text) {
        try {
            return personDao.findAbsolutText(text).stream()
                    .map(personMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User not Found");
        }

    }
}
