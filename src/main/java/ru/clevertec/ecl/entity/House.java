package ru.clevertec.ecl.entity;

import lombok.*;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.clevertec.ecl.entity.Listener.HouseListener;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EntityListeners(HouseListener.class)
@Table(name = "houses")
public class House {

}