import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exeption.EntityNotFoundException;
import ru.clevertec.ecl.service.imp.PersonServiceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImpTest {

    @Mock
    private PersonDao personDao;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImp personServiceImp;

    private UUID uuid;
    private PersonReq personReq;
    private Person person;
    private Person newPerson;
    private PersonRes personRes;

    @BeforeEach
    public void setUp() {
        uuid = UUID.randomUUID();
        personReq = new PersonReq();
        person = new Person();
        person.setId(1L);
        person.setUuid(uuid);
        person.setCreateDate(LocalDateTime.now());

        newPerson = new Person();
        newPerson.setId(person.getId());
        newPerson.setUuid(person.getUuid());
        newPerson.setHouse(person.getHouse());
        newPerson.setCreateDate(person.getCreateDate());
        newPerson.setUpdateDate(LocalDateTime.now());

        personRes = new PersonRes();

        when(personDao.findByUUID(uuid)).thenReturn(person);
        when(personMapper.toRequest(personReq)).thenReturn(newPerson);
        when(personDao.update(newPerson)).thenReturn(personRes);
    }

    @Test
    public void whenUpdatePerson() throws EntityNotFoundException {
        PersonRes updatedPerson = personServiceImp.update(uuid, personReq);

        assertEquals(personRes, updatedPerson);
        verify(personDao).findByUUID(uuid);
        verify(personMapper).toRequest(personReq);
        verify(personDao).update(newPerson);
    }

    @Test
    public void whenUpdatePersonWithNonExistingUUID() {
        UUID nonExistingUUID = UUID.randomUUID();
        when(personDao.findByUUID(nonExistingUUID)).thenThrow(new EntityNotFoundException("User not Found"));

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> personServiceImp.update(nonExistingUUID, personReq),
                "Expected update to throw, but it didn't"
        );

        assertEquals("User not Found", thrown.getMessage());
    }

    @Test
    public void testFindPersonWhoLivesInHouse() throws EntityNotFoundException {
        UUID houseId = UUID.randomUUID();
        List<Person> persons = new ArrayList<>();
        PersonRes personRes = new PersonRes();

        when(personDao.findPersonWhoLivesInHouse(houseId)).thenReturn(persons);
        when(personMapper.toResponse(any(Person.class))).thenReturn(personRes);

        personServiceImp.findPersonWhoLivesInHouse(houseId);

        verify(personDao, times(1)).findPersonWhoLivesInHouse(houseId);
        verify(personMapper, times(persons.size())).toResponse(any(Person.class));
    }

    @Test
    public void testFindAbsolutText() throws EntityNotFoundException {
        String text = "test";
        List<Person> persons = new ArrayList<>();
        PersonRes personRes = new PersonRes();

        when(personDao.findAbsolutText(text)).thenReturn(persons);
        when(personMapper.toResponse(any(Person.class))).thenReturn(personRes);

        personServiceImp.findAbsolutText(text);

        verify(personDao, times(1)).findAbsolutText(text);
        verify(personMapper, times(persons.size())).toResponse(any(Person.class));
    }

    @Test
    public void testSave() throws EntityNotFoundException {
        PersonReq personReq = new PersonReq();
        Person person = new Person();

        when(personMapper.toRequest(personReq)).thenReturn(person);

        personServiceImp.save(personReq);

        verify(personMapper, times(1)).toRequest(personReq);
        verify(personDao, times(1)).save(person);
        verify(personMapper, times(1)).toResponse(person);
    }

    @Test
    public void testFindByUUID() {
        UUID id = UUID.randomUUID();
        Person person = new Person();

        when(personDao.findByUUID(id)).thenReturn(person);

        personServiceImp.findByUUID(id);

        verify(personDao, times(1)).findByUUID(id);
        verify(personMapper, times(1)).toResponse(person);
    }

    @Test
    public void testFindByUUIDInside() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();

        personServiceImp.findByUUIDInside(id);

        verify(personDao, times(1)).findByUUID(id);
    }

    @Test
    public void testFindAll() throws EntityNotFoundException {
        int page = 1;
        int pageSize = 10;
        List<Person> persons = new ArrayList<>();

        when(personDao.findAll(page, pageSize)).thenReturn(persons);

        personServiceImp.findAll(page, pageSize);

        verify(personDao, times(1)).findAll(page, pageSize);
        verify(personMapper, times(persons.size())).toResponse(any(Person.class));
    }

    @Test
    public void testDelete() {
        UUID id = UUID.randomUUID();

        personServiceImp.delete(id);

        verify(personDao, times(1)).delete(id);
    }


}