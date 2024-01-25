package ru.clevertec.ecl.service.imp;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.PersonType;
import ru.clevertec.ecl.exeption.EntityNotFoundException;
import ru.clevertec.ecl.exeption.NotEmptyException;
import ru.clevertec.ecl.proxy.Cache;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class HouseServiceImp implements HouseService {
    private final HouseDao houseDao;
    private final HouseMapper houseMapper;
    private final PersonService personService;

    @Override
    @Cache
    public HouseRes update(UUID uuid, HouseReq houseReq) throws EntityNotFoundException {
        House house = houseDao.findByUUID(uuid);
        house.setUuid(uuid);
        house.setArea(houseReq.getArea());
        house.setCity(houseReq.getCity());
        house.setNumber(houseReq.getNumber());
        house.setStreet(houseReq.getStreet());
        house.setCountry(houseReq.getCountry());
        return houseMapper.toResponse(houseDao.save(house));
    }

    @Override
    @Cache
    public HouseRes save(HouseReq house) throws EntityNotFoundException {
        House house1 = houseMapper.toRequest(house);
        houseDao.save(house1);
        return houseMapper.toResponse(house1);
    }


    @Override
    public Page<HouseRes> findAll(int page, int pageSize) throws EntityNotFoundException {
        Page<HouseRes> housePage = houseDao.findAll(PageRequest.of(page, pageSize)).map(houseMapper::toResponse);
        if (housePage.isEmpty()) {
            throw new EntityNotFoundException("No persons found");
        }
        return housePage;
    }

    @Override
    public Page<HouseRes> findHousesWhichSomeTimeLivesPerson(UUID personId, Pageable pageable) {
        Page<House> page = houseDao.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personId, PersonType.TENANT, pageable);
        return page.map(houseMapper::toResponse);
    }

    @Override
    public Page<HouseRes> findHousesWhichSomeTimeOwnPerson(UUID personId, Pageable pageable) {
        Page<House> page = houseDao.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personId, PersonType.OWNER, pageable);
        return page.map(houseMapper::toResponse);
    }

    @Cache
    @Override
    @Transactional
    public void delete(UUID uuid) throws EntityNotFoundException {
        House house = houseDao.findByUUID(uuid);
        if (!house.getResidents().isEmpty()) {
            throw new NotEmptyException("house not Empty");
        }
        houseDao.deleteHouseByUuid(uuid);

    }

    @Override
    public List<HouseRes> findAbsolutText(String text) {
        if (!houseDao.findAbsolutText(text).isEmpty()) {
            return houseDao.findAbsolutText(text).
                    stream().map(houseMapper::toResponse).
                    collect(Collectors.toList());
        } else{
            throw new NotEmptyException("empty text");
        }
    }

    @Override
    public List<HouseRes> findOwners(UUID houseId) {
        return houseDao.findOwners(houseId).
                stream().map(houseMapper::toResponse).
                collect(Collectors.toList());
    }

    @Override
    public void buyHouse(UUID uuidHouse, UUID personId) {
        Person person = personService.findByUUIDInside(personId);
        House house = houseDao.findByUUID(uuidHouse);
        house.getOwners().add(person);
        houseDao.save(house);
        personService.saveEntity(person);
    }

    @Override
    @Cache
    public HouseRes findByUUID(UUID id) throws EntityNotFoundException {
        return houseMapper.toResponse(houseDao.findByUUID(id));
    }

}
