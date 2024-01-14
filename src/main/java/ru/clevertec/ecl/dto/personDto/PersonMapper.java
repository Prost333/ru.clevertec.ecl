package ru.clevertec.ecl.dto.personDto;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.ecl.entity.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "adress", expression = "java(person.getHouse() != null ? person.getHouse().getStreet() +" +
            " \" \" + person.getHouse().getNumber() : null)")
    PersonRes toResponse(Person person);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Person toRequest(PersonReq personRequest);

}
