package ru.clevertec.ecl.entity.Listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.ecl.entity.Person;

import java.time.LocalDateTime;
import java.util.UUID;

public class PersonListener {
    @PrePersist
    public void prePersist(Person person) {
        LocalDateTime now = LocalDateTime.now();
        person.setCreateDate(now);
        person.setUpdateDate(person.getCreateDate());
        person.setUuid(UUID.randomUUID());
    }

    @PreUpdate
    public void preUpdate(Person person) {
        person.setUpdateDate(LocalDateTime.now());
    }

}
