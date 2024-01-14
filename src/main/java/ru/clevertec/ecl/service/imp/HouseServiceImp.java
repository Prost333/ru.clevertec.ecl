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

}
