package util.constants;

import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConstantsPerson {
    public static final Long PERSON_ID = 2L;
    public static final UUID PERSON_UUID = UUID.fromString("5fbb3e36-1cfc-44a5-9eca-ef4d7c43555e");
    public static final String PERSON_NAME = "Геннa";
    public static final String PERSON_SURNAME = "Букин";
    public static final Sex PERSON_SEX = Sex.MALE;
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.now();
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2007, 12, 21, 10,10, 0);
    public static final String UPDATE_PERSON_NAME = "Кожанный";
    public static final String UPDATE_PERSON_SURNAME = "Мяч";
}