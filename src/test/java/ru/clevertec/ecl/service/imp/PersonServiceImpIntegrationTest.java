package ru.clevertec.ecl.service.imp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Passport;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;
import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceImpIntegrationTest {

    @Autowired
    private PersonServiceImp personServiceImp;

    @MockBean
    private PersonDao personDao;

    @MockBean
    private PersonMapper personMapper;

    @Test
    public void testMultithreadedAccess() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                UUID uuid = UUID.randomUUID();
                PersonReq req = new PersonReq();
                Person person = new Person();
                person.setName("Test");
                person.setSurname("Test");
                person.setSex(Sex.MALE);
                person.setPassport(new Passport("NJ","12211"));
                person.setUpdateDate(LocalDateTime.now());

                when(personDao.findPerson(uuid)).thenReturn(Optional.of(person));
                when(personMapper.toResponse(person)).thenReturn(new PersonRes());

                personServiceImp.update(uuid, req);

                personServiceImp.save(req);

                personServiceImp.saveEntity(person);

                personServiceImp.findByUUID(uuid);

                personServiceImp.findByUUIDInside(uuid);

                Pageable pageable = PageRequest.of(0, 10);
                personServiceImp.findAll(0, 10);

                PersonType type = PersonType.TENANT;
                personServiceImp.findByPersonByHistory(uuid, type, pageable);

                personServiceImp.delete(uuid);

                personServiceImp.findPersonWhoLivesInHouse(uuid);

                personServiceImp.findAbsolutText("Test");
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

}
