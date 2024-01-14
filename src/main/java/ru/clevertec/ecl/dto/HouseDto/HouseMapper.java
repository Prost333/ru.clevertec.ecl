package ru.clevertec.ecl.dto.HouseDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.entity.House;
@Mapper(componentModel = "spring")
public interface HouseMapper {
    HouseRes toResponse(House house);

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    House toRequest(HouseReq houseReq);
}
