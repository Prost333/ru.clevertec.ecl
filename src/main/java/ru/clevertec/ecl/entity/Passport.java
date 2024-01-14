package ru.clevertec.ecl.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import jakarta.persistence.Embeddable;
import jakarta.persistence.UniqueConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"passport_series", "passport_number"}))
public class Passport {

}
