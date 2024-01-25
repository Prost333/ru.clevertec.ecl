package util.constants;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConstantsHouse {
    public static final Long HOUSE_ID = 3L;
    public static final UUID HOUSE_UUID = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272");
    public static final String HOUSE_AREA = "123";
    public static final String HOUSE_COUNTRY = "by";
    public static final String HOUSE_CITY = "Пропойск";
    public static final String HOUSE_STREET = "Ком Туп";
    public static final Integer HOUSE_NUMBER = 23;
    public static final LocalDateTime HOUSE_CREATE_DATE = LocalDateTime.now();
    public static final String UPDATE_HOUSE_AREA = "333";
    public static final String UPDATE_HOUSE_CITY = "Славгород";
    public static final UUID INCORRECT_UUID = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8000");

}
