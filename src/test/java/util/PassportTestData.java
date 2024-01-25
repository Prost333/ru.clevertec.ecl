package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.entity.Passport;

import static util.constants.ConstantsPassport.NUMBER;
import static util.constants.ConstantsPassport.SERIES;

@Data
@Builder(setterPrefix = "with")
public class PassportTestData {

    @Builder.Default
    String series = SERIES;

    @Builder.Default
    String number = NUMBER;

    public Passport getPassport(){
        return new Passport(series, number);
    }
}
