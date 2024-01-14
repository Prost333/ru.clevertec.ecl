package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.UUID;

public interface HouseService {
    HouseRes update(UUID uuid,HouseReq houseReq);

    HouseRes findById(Long id);

    HouseRes save(HouseReq house);

    HouseRes findByUUID (UUID id);
    List<HouseRes> findAll(int page, int pageSize);

    void delete(UUID uuid);
    List<HouseRes> findAbsolutText(String text);
    List<HouseRes> findOwners(UUID houseId);
    void buyHouse(UUID uuidHouse, UUID personId);

}
