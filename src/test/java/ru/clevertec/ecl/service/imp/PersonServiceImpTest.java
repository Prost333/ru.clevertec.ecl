package ru.clevertec.ecl.service.imp;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonServiceImpTest {

    @InjectMocks
    private PersonServiceImp personServiceImp;

    @Mock
    private PersonDao personDao;

    @Mock
    private PersonMapper personMapper;

    @Test
    public void testUpdate() {
        UUID uuid = UUID.randomUUID();
        PersonReq req = new PersonReq();
        Person person = new Person();
        when(personDao.findPerson(uuid)).thenReturn(Optional.of(person));
        when(personMapper.toResponse(person)).thenReturn(new PersonRes());
        PersonRes result = personServiceImp.update(uuid, req);
        assertEquals(new PersonRes(), result);
    }

    @Test
    public void testSave() {
        PersonReq personReq = new PersonReq();
        Person person = new Person();
        when(personMapper.toRequest(personReq)).thenReturn(person);
        when(personMapper.toResponse(person)).thenReturn(new PersonRes());
        PersonRes result = personServiceImp.save(personReq);
        assertEquals(new PersonRes(), result);
    }

    @Test
    public void testSaveEntity() {
        Person person = new Person();
        when(personDao.save(person)).thenReturn(person);
        Person result = personServiceImp.saveEntity(person);
        assertEquals(person, result);
    }

    @Test
    public void testFindByUUID() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person();
        when(personDao.findPerson(uuid)).thenReturn(Optional.of(person));
        when(personMapper.toResponse(person)).thenReturn(new PersonRes());
        PersonRes result = personServiceImp.findByUUID(uuid);
        assertEquals(new PersonRes(), result);
    }

    @Test
    public void testFindByUUIDInside() {
        UUID id = UUID.randomUUID();
        Person person = new Person();
        when(personDao.findPerson(id)).thenReturn(Optional.of(person));
        Person result = personServiceImp.findByUUIDInside(id);
        assertEquals(person, result);
    }

    @Test
    public void testFindAll() {
        int page = 0;
        int pageSize = 10;
        Page<Person> personsPage = new PageImpl<>(Collections.singletonList(new Person()));
        when(personDao.findAll(PageRequest.of(page, pageSize))).thenReturn(personsPage);
        when(personMapper.toResponse(Mockito.any(Person.class))).thenReturn(new PersonRes());
        Page<PersonRes> result = personServiceImp.findAll(page, pageSize);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindByPersonByHistory() {
        UUID uuid = UUID.randomUUID();
        PersonType type = PersonType.TENANT;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Person> personsPage = new PageImpl<>(Collections.singletonList(new Person()));
        when(personDao.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(uuid, type, pageable)).thenReturn(personsPage);
        when(personMapper.toResponse(Mockito.any(Person.class))).thenReturn(new PersonRes());
        Page<PersonRes> result = personServiceImp.findByPersonByHistory(uuid, type, pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testDelete() {
        UUID id = UUID.randomUUID();
        Person person = new Person();
        when(personDao.findPerson(id)).thenReturn(Optional.of(person));
        assertDoesNotThrow(() -> personServiceImp.delete(id));
    }

    @Test
    public void testFindPersonWhoLivesInHouse() {
        UUID houseId = UUID.randomUUID();
        List<Person> persons = Collections.singletonList(new Person());
        when(personDao.findPersonWhoLivesInHouse(houseId)).thenReturn(persons);
        when(personMapper.toResponse(Mockito.any(Person.class))).thenReturn(new PersonRes());
        List<PersonRes> result = personServiceImp.findPersonWhoLivesInHouse(houseId);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindAbsolutText() {
        String text = "test";
        List<Person> persons = Collections.singletonList(new Person());
        when(personDao.findAbsolutText(text)).thenReturn(persons);
        when(personMapper.toResponse(Mockito.any(Person.class))).thenReturn(new PersonRes());
        List<PersonRes> result = personServiceImp.findAbsolutText(text);
        assertEquals(1, result.size());
    }

}