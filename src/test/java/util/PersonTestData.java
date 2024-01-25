package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.HouseHistory;
import ru.clevertec.ecl.entity.Passport;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static util.constants.ConstantsPerson.*;

@Data
@Builder(setterPrefix = "with")
public class PersonTestData {

    @Builder.Default
    private Long id = PERSON_ID;

    @Builder.Default
    private UUID uuid = PERSON_UUID;

    @Builder.Default
    private String name = PERSON_NAME;

    @Builder.Default
    private String surname = PERSON_SURNAME;

    @Builder.Default
    private Sex sex = PERSON_SEX;

    @Builder.Default
    private Passport passport = PassportTestData.builder()
            .build()
            .getPassport();

    @Builder.Default
    private LocalDateTime createDate = PERSON_CREATE_DATE;

    @Builder.Default
    private LocalDateTime updateDate = PERSON_UPDATE_DATE;

    @Builder.Default
    private House house = HouseTestData.builder()
            .build()
            .getEntity();

    @Builder.Default
    private List<House> ownedHouses = List.of();

    @Builder.Default
    private Set<HouseHistory> personHouseHistories = Set.of();

    public Person getEntity() {
        return new Person(id, uuid, name, surname, sex, passport, createDate, updateDate, house, ownedHouses, personHouseHistories);
    }

    public PersonReq getRequestDto() {
        return new PersonReq(name, surname, sex, passport, house.getUuid(), ownedHouses);
    }

    public Optional<Person> getOptionalEntity() {
        return Optional.of(getEntity());
    }

    public PersonRes getResponseDto() {
        return new PersonRes(uuid, passport,name,surname,sex, getResponseDto().getAdress(), createDate,updateDate);
    }
}