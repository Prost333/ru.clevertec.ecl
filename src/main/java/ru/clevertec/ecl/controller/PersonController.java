package ru.clevertec.ecl.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.dto.ResponsePage;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.enums.PersonType;
import ru.clevertec.ecl.service.PersonService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/getAll")
    public ResponsePage<PersonRes> personList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Page<PersonRes> personPage = personService.findAll(page, pageSize);
        return new ResponsePage<>(personPage);
    }

    @PostMapping
    public ResponseEntity<PersonRes> save(@RequestBody PersonReq personReq) {
        return ResponseEntity.ok(personService.save(personReq));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonRes> findByUUId(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(personService.findByUUID(uuid));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        personService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/livesInHouse/")
    public ResponseEntity<List<PersonRes>> findPersonWhoLivesInHouse(@RequestParam(name = "houseId") UUID houseId) {
        return ResponseEntity.ok(personService.findPersonWhoLivesInHouse(houseId));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PersonRes> update(@PathVariable(name = "uuid") UUID uuid,
                                            @RequestBody PersonReq personReq) {
        return ResponseEntity.ok(personService.update(uuid, personReq));
    }

    @GetMapping("/search/{text}")
    public ResponseEntity<List<PersonRes>> findAbsolutText (@PathVariable(name = "text") String text) {
        return ResponseEntity.ok(personService.findAbsolutText(text));
    }

    @GetMapping("/SomeTimeOwnPerson/{uuid}")
    public ResponseEntity<Page<PersonRes>> findPersonWhichSomeTimeOwnHouse(@PathVariable(name = "uuid")UUID uuid, @PageableDefault(10) Pageable pageable) {
        Page page= personService.findByPersonByHistory(uuid, PersonType.OWNER,pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/SomeTimeTenantPerson/{uuid}")
    public ResponseEntity<Page<PersonRes>> findPersonWhichSomeTimeTenantHouse(@PathVariable(name = "uuid")UUID uuid, @PageableDefault(10) Pageable pageable) {
        Page page= personService.findByPersonByHistory(uuid, PersonType.TENANT,pageable);
        return ResponseEntity.ok(page);
    }

}
