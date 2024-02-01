package ru.clevertec.ecl.service.imp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

@SpringBootTest
public class HouseServiceImpIntegrationTest {

    @Autowired
    private HouseServiceImp houseServiceImp;

    @MockBean
    private HouseDao houseDao;

    @MockBean
    private HouseMapper houseMapper;

    @MockBean
    private PersonService personService;

    @Test
    public void testMultithreadedAccess() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                UUID uuid = UUID.randomUUID();
                HouseReq houseReq = new HouseReq();
                houseReq.setArea("Test");
                houseReq.setCity("Test");
                houseReq.setNumber(1);
                houseReq.setStreet("Test");
                houseReq.setCountry("Test");
                House house = new House();
                house.setUuid(uuid);
                house.setArea(houseReq.getArea());
                house.setCity(houseReq.getCity());
                house.setNumber(houseReq.getNumber());
                house.setStreet(houseReq.getStreet());
                house.setCountry(houseReq.getCountry());

                when(houseDao.findByUUID(uuid)).thenReturn(house);
                when(houseMapper.toResponse(house)).thenReturn(new HouseRes());

                // Вызов метода update
                houseServiceImp.update(uuid, houseReq);

                // Вызов метода save
                houseServiceImp.save(houseReq);

                // Вызов метода findAll
                Pageable pageable = PageRequest.of(0, 10);
                houseServiceImp.findAll(0, 10);

                // Вызов метода findHousesWhichSomeTimeLivesPerson
                houseServiceImp.findHousesWhichSomeTimeLivesPerson(uuid, pageable);

                // Вызов метода findHousesWhichSomeTimeOwnPerson
                houseServiceImp.findHousesWhichSomeTimeOwnPerson(uuid, pageable);

                // Вызов метода delete
                houseServiceImp.delete(uuid);

                // Вызов метода findAbsolutText
                houseServiceImp.findAbsolutText("Test");

                // Вызов метода findOwners
                houseServiceImp.findOwners(uuid);

                // Вызов метода buyHouse
                houseServiceImp.buyHouse(uuid, uuid);

                // Вызов метода findByUUID
                houseServiceImp.findByUUID(uuid);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

}
