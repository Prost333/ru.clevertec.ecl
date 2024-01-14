package ru.clevertec.ecl.dao;


import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.UUID;

public interface HouseDao {

    HouseRes update(House o);

    HouseRes findById(Long id);

    void save(House house);

    List<HouseRes> findAll(int page, int pageSize);

    void delete(UUID uuid);

    House findByUUID(UUID id);
    List<House> findAbsolutText(String text);
    List<House> findOwners(UUID houseId);

}
