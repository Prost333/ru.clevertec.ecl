package ru.clevertec.ecl.service.imp;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exeption.EntityNotFoundException;
import ru.clevertec.ecl.service.PersonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonServiceImp implements PersonService {

    private final PersonDao personDao;

    private final PersonMapper personMapper;

    @Override
    public PersonRes update(UUID uuid, PersonReq req)throws EntityNotFoundException {
        Person person = personDao.findByUUID(uuid);
        Person newPerson = personMapper.toRequest(req);
        newPerson.setId(person.getId());
        newPerson.setUuid(uuid);
        newPerson.setHouse(person.getHouse());
        newPerson.setCreateDate(person.getCreateDate());
        newPerson.setUpdateDate(LocalDateTime.now());
        return personDao.update(newPerson);
    }

    @Override
    public Person findById(Long id) {
        Optional<Person> p = Optional.ofNullable(personDao.findById(id));
        return p.orElseThrow(()->new EntityNotFoundException("User not Found"));
    }

    @Override
    public PersonRes save(PersonReq personReq) throws EntityNotFoundException {
        Person person = personMapper.toRequest(personReq);
        personDao.save(person);
        return personMapper.toResponse(person);
    }

    @Override
    public PersonRes findByUUID(UUID id) {
        Optional<Person> p = Optional.ofNullable(personDao.findByUUID(id));
        return personMapper.toResponse(p.orElseThrow(()->new EntityNotFoundException("User not Found")));

    }

    @Override
    public Person findByUUIDInside(UUID id)  throws EntityNotFoundException{
        return personDao.findByUUID(id);
    }

    @Override
    public List<PersonRes> findAll(int page, int pageSize) throws EntityNotFoundException {
        return personDao.findAll(page, pageSize).
                stream().map(personMapper::toResponse).
                collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        try {
            personDao.delete(id);
        }catch (EntityNotFoundException e){
            throw  new EntityNotFoundException("User not Found");
        }

    }

    @Override
    public List<PersonRes> findPersonWhoLivesInHouse(UUID houseId)throws EntityNotFoundException  {

        return personDao.findPersonWhoLivesInHouse(houseId).
                stream().map(personMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonRes> findAbsolutText(String text) {
        try {
            return personDao.findAbsolutText(text).stream().map(personMapper::toResponse).collect(Collectors.toList());
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User not Found");
        }

    }
}
