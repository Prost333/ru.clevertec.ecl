package ru.clevertec.ecl.service.imp;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;
import ru.clevertec.ecl.exeption.EntityNotFoundException;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HouseServiceImpTest {

    @Autowired
    private HouseService houseService;

    @MockBean
    private HouseDao houseDao;

    @MockBean
    private HouseMapper houseMapper;

    @MockBean
    private PersonService personService;

    @Test
    public void testUpdate() {
        HouseReq mockHouseReq = mock(HouseReq.class);
        House mockHouse = mock(House.class);

        when(mockHouseReq.getArea()).thenReturn("New Area");
        when(mockHouseReq.getCity()).thenReturn("New City");
        when(mockHouseReq.getNumber()).thenReturn(1);
        when(mockHouseReq.getStreet()).thenReturn("New Street");
        when(mockHouseReq.getCountry()).thenReturn("New Country");

        when(houseDao.findByUUID(any(UUID.class))).thenReturn(mockHouse);
        when(houseDao.save(any(House.class))).thenReturn(mockHouse);
        when(houseMapper.toResponse(any(House.class))).thenReturn(new HouseRes());


        UUID uuid = UUID.randomUUID();

        HouseRes result = houseService.update(uuid, mockHouseReq);

        verify(mockHouse).setArea("New Area");
        verify(mockHouse).setCity("New City");
        verify(mockHouse).setNumber(1);
        verify(mockHouse).setStreet("New Street");
        verify(mockHouse).setCountry("New Country");

        verify(houseDao).save(mockHouse);

        assertNotNull(result);
    }


    @Test
    public void testFindByUUID() {
        UUID uuid = UUID.randomUUID();
        House mockHouse = mock(House.class);
        when(houseDao.findByUUID(uuid)).thenReturn(mockHouse);
        when(houseMapper.toResponse(mockHouse)).thenReturn(new HouseRes());
        HouseRes result = houseService.findByUUID(uuid);
        assertEquals(new HouseRes(), result);
    }

    @Test
    public void testFindOwners() {
        UUID houseId = UUID.randomUUID();
        List<House> houses = Collections.singletonList(new House());
        when(houseDao.findOwners(houseId)).thenReturn(houses);
        when(houseMapper.toResponse(Mockito.any(House.class))).thenReturn(new HouseRes());
        List<HouseRes> result = houseService.findOwners(houseId);
        assertEquals(1, result.size());
    }
    @Test
    public void testFindAll() throws EntityNotFoundException {
        int page = 0;
        int pageSize = 10;
        List<House> houses = new ArrayList<>();
        House house = new House();
        house.setUuid(UUID.randomUUID());
        house.setArea("Test Area");
        house.setCity("Test City");
        house.setNumber(1);
        house.setStreet("Test Street");
        house.setCountry("Test Country");
        houses.add(house);
        Page<House> housePage = new PageImpl<>(houses);

        when(houseDao.findAll(PageRequest.of(page, pageSize))).thenReturn(housePage);
        when(houseMapper.toResponse(any(House.class))).thenReturn(new HouseRes());

        Page<HouseRes> result = houseService.findAll(page, pageSize);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(houseDao, times(1)).findAll(PageRequest.of(page, pageSize));
    }
    @Test
    public void testSave() throws EntityNotFoundException {
        HouseReq houseReq = new HouseReq();
        houseReq.setArea("121");
        houseReq.setCity("Gomel");
        houseReq.setCountry("by");
        House house = new House();
        house.setArea("121");
        house.setCity("Gomel");
        house.setCountry("by");

        when(houseMapper.toRequest(any(HouseReq.class))).thenReturn(house);
        when(houseDao.save(any(House.class))).thenReturn(house);
        when(houseMapper.toResponse(any(House.class))).thenReturn(new HouseRes());

        HouseRes result = houseService.save(houseReq);

        assertNotNull(result);
        verify(houseDao, times(1)).save(any(House.class));
    }

    @Test
    public void testFindHousesWhichSomeTimeLivesPerson() {
        UUID personId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<House> housePage = new PageImpl<>(new ArrayList<>());

        when(houseDao.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personId, PersonType.TENANT, pageable)).thenReturn(housePage);
        when(houseMapper.toResponse(any(House.class))).thenReturn(new HouseRes());

        Page<HouseRes> result = houseService.findHousesWhichSomeTimeLivesPerson(personId, pageable);

        assertNotNull(result);
        verify(houseDao, times(1)).findByHouseHistoriesPersonUuidAndHouseHistoriesType(personId, PersonType.TENANT, pageable);
    }

    @Test
    public void testDelete() throws EntityNotFoundException {
        UUID uuid = UUID.randomUUID();
        House house = new House();
        house.setResidents(new HashSet<>());

        when(houseDao.findByUUID(uuid)).thenReturn(house);

        houseService.delete(uuid);

        verify(houseDao, times(1)).deleteHouseByUuid(uuid);
    }

    @Test
    public void testFindAbsolutText() {
        String text = "test";
        List<House> houses = new ArrayList<>();

        when(houseDao.findAbsolutText(text)).thenReturn(houses);
        when(houseMapper.toResponse(any(House.class))).thenReturn(new HouseRes());

        List<HouseRes> result = houseService.findAbsolutText(text);

        assertNotNull(result);
        verify(houseDao, times(1)).findAbsolutText(text);
    }

}
