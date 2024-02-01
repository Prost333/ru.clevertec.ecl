package ru.clevertec.ecl.dao;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.enums.PersonType;
import util.HouseTestData;
import util.PersonTestData;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class HouseDaoTest {

    @Autowired
    private HouseDao houseDao;
    private HouseTestData houseTestData = HouseTestData.builder().build();

    private PersonTestData personTestData = PersonTestData.builder().build();

    @Test
    public void testFindByUUID() throws Exception {
        UUID uuid = houseTestData.getUuid();

        House house = houseDao.findByUUID(uuid);
        assertNotNull(house);
        assertEquals(uuid, house.getUuid());
    }

    @Test
    public void testFindAbsolutText() throws Exception {
        String text = houseTestData.getCity();

        List<House> houses = houseDao.findAbsolutText(text);
        assertFalse(houses.isEmpty());

    }

    @Test
    public void testFindOwners() throws Exception {
        UUID uuid = personTestData.getUuid();

        List<House> houses = houseDao.findOwners(uuid);
        assertFalse(houses.isEmpty());

    }

    @Test
    public void testFindAll() throws Exception {

        Page<House> houses = houseDao.findAll(PageRequest.of(0, 10));
        assertFalse(houses.isEmpty());
    }

    @Test
    public void testFindByHouseHistoriesPersonUuidAndHouseHistoriesType() throws Exception {
        UUID uuid = personTestData.getUuid();
        PersonType type = PersonType.TENANT;
        Page<House> houses = houseDao.findByHouseHistoriesPersonUuidAndHouseHistoriesType(uuid, type, PageRequest.of(0, 10));
        assertFalse(houses.isEmpty());

    }

    @Test
    public void testDeleteHouseByUuid() throws Exception {
        UUID uuid = UUID.randomUUID();

        houseDao.deleteHouseByUuid(uuid);
        House house = houseDao.findByUUID(uuid);
        assertNull(house);
    }
}