package ru.clevertec.ecl.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.dto.ResponsePage;
import ru.clevertec.ecl.service.HouseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/house")
@AllArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/{uuid}")
    public  ResponseEntity<HouseRes> findById(@PathVariable(name = "uuid") UUID uuid) {
        return ResponseEntity.ok(houseService.findByUUID(uuid));
    }

    @PostMapping
    public HouseRes save(@RequestBody HouseReq houseReq) {
        return houseService.save(houseReq);
    }

    @GetMapping
    public ResponsePage<HouseRes> findAll(@RequestParam(defaultValue = "0", name = "page") int page,
                                          @RequestParam(defaultValue = "10", name = "pageSize") int pageSize) {
        Page<HouseRes> housePage = houseService.findAll(page, pageSize);
        return new ResponsePage<>(housePage);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable(name = "uuid") UUID uuid) {
        houseService.delete(uuid);
    }

    @PutMapping("/{uuid}")
    public  ResponseEntity<HouseRes> update(@PathVariable(name = "uuid") UUID uuid,
                           @RequestBody HouseReq houseReq) {
        return ResponseEntity.ok(houseService.update(uuid, houseReq));
    }

    @GetMapping("/lives/{uuid}")
    public ResponseEntity<List<HouseRes>> findHousesWhichOwnPerson(@PathVariable(name = "uuid") UUID uuid) {
        return ResponseEntity.ok(houseService.findOwners(uuid));
    }

    @GetMapping("/search/{text}")
    public ResponseEntity<List<HouseRes>> findAbsolutText(@PathVariable(name = "text") String text) {

        return ResponseEntity.ok(houseService.findAbsolutText(text));
    }

    @PutMapping("Buy/{uuid}/{personId}")
    public void buyHouse(@PathVariable(name = "uuid") UUID uuid, @PathVariable(name = "personId") UUID personId) {
        houseService.buyHouse(uuid, personId);
    }

    @GetMapping("/SomeTimeLivesPerson/{personID}")
    public ResponseEntity<Page<HouseRes>> findHousesWhichSomeTimeLivesPerson(@PathVariable(name = "personID")UUID personId, @PageableDefault(10) Pageable pageable) {
        Page page= houseService.findHousesWhichSomeTimeLivesPerson(personId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/SomeTimeOwnPerson/{personID}")
    public ResponseEntity<Page<HouseRes>> findHousesWhichSomeTimeOwnPerson(@PathVariable(name = "personID")UUID personId, @PageableDefault(10) Pageable pageable) {
        Page page= houseService.findHousesWhichSomeTimeOwnPerson(personId, pageable);
        return ResponseEntity.ok(page);
    }
}
