package ru.clevertec.ecl.dto.HouseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseRes {
    private String area;
    private UUID uuid;
    private Integer number;
    private String city;
    private String street;
    private String country;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime createDate;

    public HouseRes(UUID uuid, Integer number, String city, String street, String country, LocalDateTime createDate) {
        this.uuid = uuid;
        this.number = number;
        this.city = city;
        this.street = street;
        this.country = country;
        this.createDate = createDate;
    }
}
