package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.HouseHistory;
import ru.clevertec.ecl.entity.Person;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static util.constants.ConstantsHouse.*;

@Data
@Builder(setterPrefix = "with")
public class HouseTestData {

    @Builder.Default
    private Long id = HOUSE_ID;

    @Builder.Default
    private UUID uuid = HOUSE_UUID;

    @Builder.Default
    private String area = HOUSE_AREA;

    @Builder.Default
    private String country = HOUSE_COUNTRY;

    @Builder.Default
    private String city = HOUSE_CITY;

    @Builder.Default
    private String street = HOUSE_STREET;

    @Builder.Default
    private Integer number = HOUSE_NUMBER;

    @Builder.Default
    private LocalDateTime createDate = HOUSE_CREATE_DATE;

    @Builder.Default
    private Set<Person> residents = Set.of();

    @Builder.Default
    private Set<Person> owners = Set.of();

    @Builder.Default
    private Set<HouseHistory> houseHistories = Set.of();

    public House getEntity() {
        return new House(id, uuid, area, country, city, street, number, createDate, residents, owners, houseHistories);
    }

    public HouseReq getRequestDto() {
        return new HouseReq(area,country,city,street,number);
    }

    public Optional<House> getOptionalEntity() {
        return Optional.of(getEntity());
    }

    public HouseRes getResponseDto() {
        return new HouseRes(uuid,number,city,street,country,createDate);
    }
}
