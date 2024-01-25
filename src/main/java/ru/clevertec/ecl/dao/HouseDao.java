package ru.clevertec.ecl.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.enums.PersonType;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseDao extends JpaRepository<House, UUID> {

    @Query("SELECT h FROM House h WHERE h.uuid = :uuid")
    House findByUUID(@Param("uuid") UUID id);

    @Query("SELECT h FROM House h WHERE h.area LIKE %:text% OR h.country LIKE %:text% OR h.city LIKE %:text% OR h.street LIKE %:text%")
    List<House> findAbsolutText(@Param("text") String text);

    @Query("SELECT h FROM House h JOIN h.owners o WHERE o.uuid = :uuid")
    List<House> findOwners(@Param("uuid") UUID uuid);
    Page<House> findAll(Pageable pageable);


    Page<House> findByHouseHistoriesPersonUuidAndHouseHistoriesType(UUID uuid, PersonType type, Pageable pageable);

    @EntityGraph(attributePaths = {"residents", "houseHistories", "owners"})
    void deleteHouseByUuid(UUID uuid);

}
