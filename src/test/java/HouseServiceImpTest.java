import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.service.PersonService;
import ru.clevertec.ecl.service.imp.HouseServiceImp;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class HouseServiceImpTest {

    @Mock
    private HouseDao houseDao;

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private PersonService personService;

    @InjectMocks
    private HouseServiceImp houseService;

    @Test
    void findByIdTest() {

        Long id = 1L;
        HouseRes mockHouseRes = mock(HouseRes.class);
        when(houseDao.findById(id)).thenReturn(mockHouseRes);

        HouseRes result = houseService.findById(id);

        verify(houseDao).findById(id);
        assertEquals(mockHouseRes, result);
    }

    @Test
    void saveTest() {

        HouseReq houseReq = mock(HouseReq.class);
        House house = mock(House.class);
        HouseRes houseRes = mock(HouseRes.class);
        when(houseMapper.toRequest(houseReq)).thenReturn(house);
        when(houseMapper.toResponse(house)).thenReturn(houseRes);

        HouseRes result = houseService.save(houseReq);

        verify(houseDao).save(house);
        assertEquals(houseRes, result);
    }

    @Test
    void findAllTest() {
        int page = 0;
        int pageSize = 10;
        List<HouseRes> mockHouseList = Arrays.asList(mock(HouseRes.class), mock(HouseRes.class));
        when(houseDao.findAll(page, pageSize)).thenReturn(mockHouseList);

        List<HouseRes> result = houseService.findAll(page, pageSize);

        verify(houseDao).findAll(page, pageSize);
        assertEquals(mockHouseList, result);
    }

    @Test
    void deleteTest() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        houseService.delete(uuid);

        // Assert
        verify(houseDao).delete(uuid);
    }

    @Test
    void findAbsolutTextTest() {
        String text = "search text";
        List<House> houses = Arrays.asList(new House(), new House());
        List<HouseRes> houseResponses = Arrays.asList(new HouseRes(), new HouseRes());
        when(houseDao.findAbsolutText(text)).thenReturn(houses);
        when(houseMapper.toResponse(ArgumentMatchers.any(House.class))).thenReturn(new HouseRes());

        List<HouseRes> result = houseService.findAbsolutText(text);

        verify(houseDao).findAbsolutText(text);
        verify(houseMapper, times(houses.size())).toResponse(any(House.class));
        assertEquals(houseResponses.size(), result.size());
    }

    @Test
    void findOwnersTest() {
        UUID houseId = UUID.randomUUID();
        List<House> ownersHouses = Arrays.asList(new House(), new House());
        List<HouseRes> ownersHouseResponses = Arrays.asList(new HouseRes(), new HouseRes());
        when(houseDao.findOwners(houseId)).thenReturn(ownersHouses);
        when(houseMapper.toResponse(ArgumentMatchers.any(House.class))).thenReturn(new HouseRes());

        List<HouseRes> result = houseService.findOwners(houseId);

        verify(houseDao).findOwners(houseId);
        verify(houseMapper, times(ownersHouses.size())).toResponse(any(House.class));
        assertEquals(ownersHouseResponses.size(), result.size());
    }

    @Test
    void buyHouseTest() {
        UUID uuidHouse = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        Person person = mock(Person.class);
        House house = mock(House.class);
        when(personService.findByUUIDInside(personId)).thenReturn(person);
        when(houseDao.findByUUID(uuidHouse)).thenReturn(house);
        List<Person> owners = new ArrayList<>();
        when(house.getOwners()).thenReturn(owners);

        houseService.buyHouse(uuidHouse, personId);

        verify(personService).findByUUIDInside(personId);
        verify(houseDao).findByUUID(uuidHouse);
        assertTrue(owners.contains(person));
        verify(houseDao).update(house);
    }
}