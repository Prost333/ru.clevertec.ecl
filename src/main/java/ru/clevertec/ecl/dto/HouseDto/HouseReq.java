package ru.clevertec.ecl.dto.HouseDto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.entity.Person;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseReq {
    @NotBlank
    @Size(max = 50)
    private String area;

    @NotBlank
    @Size(max = 50)
    private String country;

    @NotBlank
    @Size(max = 50)
    private String city;

    @NotBlank
    @Size(max = 50)
    private String street;

    @NotNull
    @Size(max = 30)
    private Integer number;

    @Valid
    @Builder.Default
    private List<Person> residents = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<Person> owners = new ArrayList<>();

    public HouseReq(String area, String country, String city, String street, Integer number) {


    }
}
