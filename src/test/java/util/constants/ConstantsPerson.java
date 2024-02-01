package util.constants;

import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConstantsPerson {
    public static final Long PERSON_ID = 2L;
    public static final UUID PERSON_UUID = UUID.fromString("3df38f0a-09bb-4bbc-a80c-2f827b6f9d75");
    public static final String PERSON_NAME = "Тузик";
    public static final String PERSON_SURNAME = "Вофкалов";
    public static final Sex PERSON_SEX = Sex.MALE;
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.now();
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2007, 12, 21, 10,10, 0);

}