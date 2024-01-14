package ru.clevertec.ecl.service.imp;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exeption.EntityNotFoundException;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HouseServiceImp implements HouseService {
    private final HouseDao houseDao;
    private final HouseMapper houseMapper;
    private  final PersonService personService;

    @Override
    public HouseRes update(UUID uuid, HouseReq houseReq) throws EntityNotFoundException {
        House house = houseDao.findByUUID(uuid);
        House newHouse= houseMapper.toRequest(houseReq);
        newHouse.setUuid(uuid);
        newHouse.setId(house.getId());
        newHouse.setCreateDate(house.getCreateDate());
        newHouse.setOwners(house.getOwners());
        newHouse.setResidents(house.getResidents());
        return houseDao.update(newHouse);
    }

    @Override
    public HouseRes findById(Long id) throws EntityNotFoundException {
        return houseDao.findById(id);
    }

    @Override
    public HouseRes save(HouseReq house) throws EntityNotFoundException {
        House house1 = houseMapper.toRequest(house);
        houseDao.save(house1);
        return houseMapper.toResponse(house1);
    }


    @Override
    public List<HouseRes> findAll(int page, int pageSize) throws EntityNotFoundException {
        return houseDao.findAll(page, pageSize);
    }

    @Override
    public void delete(UUID uuid) throws EntityNotFoundException {
        houseDao.delete(uuid);
    }

    @Override
    public List<HouseRes> findAbsolutText(String text) {
        return houseDao.findAbsolutText(text).
                stream().map(houseMapper::toResponse).
                collect(Collectors.toList());
    }

    @Override
    public List<HouseRes> findOwners(UUID houseId) {
        return houseDao.findOwners(houseId).
                stream().map(houseMapper::toResponse).
                collect(Collectors.toList());
    }

    @Override
    public void buyHouse(UUID uuidHouse,UUID personId) {
        Person person=personService.findByUUIDInside(personId);
        House house = houseDao.findByUUID(uuidHouse);
        house.getOwners().add(person);
        houseDao.update(house);
    }

    @Override
    public HouseRes findByUUID(UUID id) throws EntityNotFoundException {
        return houseMapper.toResponse(houseDao.findByUUID(id));
    }
}
