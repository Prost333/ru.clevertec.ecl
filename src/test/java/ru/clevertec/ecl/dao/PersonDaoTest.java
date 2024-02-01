package ru.clevertec.ecl.dao;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;
import util.HouseTestData;
import util.PersonTestData;
import util.PostgresqlTestContainer;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
public class PersonDaoTest {

    @Autowired
    private PersonDao personDao;

    @Test
    public void testFindPerson() throws Exception {
        PersonTestData personTestData = PersonTestData.builder().build();
        Optional<Person> person = personDao.findPerson(personTestData.getUuid());
        assertEquals(personTestData.getUuid(), person.get().getUuid());
    }

    @Test
    public void testFindPersonWhoLivesInHouse() throws Exception {
        HouseTestData houseTestData = HouseTestData.builder().build();
        UUID houseId = houseTestData.getUuid();
        List<Person> persons = personDao.findPersonWhoLivesInHouse(houseId);
        assertFalse(persons.isEmpty());

    }

    @Test
    public void testFindAbsolutText() throws Exception {
        PersonTestData personTestData = PersonTestData.builder().build();
        String text = personTestData.getName();


        List<Person> persons = personDao.findAbsolutText(text);
        assertFalse(persons.isEmpty());

    }

    @Test
    public void testFindByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType() throws Exception {
        HouseTestData houseTestData = HouseTestData.builder().build();
        UUID uuid = houseTestData.getUuid();
        PersonType type = PersonType.TENANT;

        Page<Person> persons = personDao.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(uuid, type, PageRequest.of(0, 10));
        assertFalse(persons.isEmpty());

    }

    @Test
    public void testFindAll() throws Exception {
        long size = 10;

        Page<Person> persons = personDao.findAll(PageRequest.of(0, 10));
        assertFalse(persons.isEmpty());


    }
}