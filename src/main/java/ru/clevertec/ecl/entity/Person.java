package ru.clevertec.ecl.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.ecl.enim.Sex;
import ru.clevertec.ecl.entity.Listener.PersonListener;

import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EntityListeners(PersonListener.class)
@Table(name = "persons")
public class Person {

}


