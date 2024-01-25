package ru.clevertec.ecl.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.ecl.entity.Listener.PersonListener;
import ru.clevertec.ecl.enums.PersonType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EntityListeners(PersonListener.class)
@Table(name = "houseHistory")
public class HouseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "type_id")
    private PersonType type;

}
