package ru.clevertec.ecl.dto.personDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.enim.Sex;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Passport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonReq {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    private Sex sex;

    @NotNull
    private Passport passport;

    @NotNull
    private UUID houseUUID;

    @Valid
    @Builder.Default
    private List<House> ownedHouses = new ArrayList<>();
}
