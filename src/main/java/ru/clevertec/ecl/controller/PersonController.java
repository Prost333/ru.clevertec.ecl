package ru.clevertec.ecl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.service.PersonService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/getAll")
    public ResponseEntity<List<PersonRes>> personList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return ResponseEntity.ok(personService.findAll(page, pageSize));
    }

    @PostMapping
    public PersonRes save(@RequestBody PersonReq personReq) {
        return personService.save(personReq);
    }

    @GetMapping("/{uuid}")
    public PersonRes findById(@PathVariable("uuid") UUID uuid) {
        return personService.findByUUID(uuid);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        personService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/livesInHouse/")
    public List<PersonRes> findPersonWhoLivesInHouse(@RequestParam(name = "houseId") UUID houseId) {
        return personService.findPersonWhoLivesInHouse(houseId);
    }

    @PutMapping("/{uuid}")
    public PersonRes update(@PathVariable(name = "uuid") UUID uuid,
                                            @RequestBody PersonReq personReq) {
        return personService.update(uuid, personReq);
    }

    @GetMapping("/search/{text}")
    public List<PersonRes> findAbsolutText (@PathVariable(name = "text") String text) {
        return personService.findAbsolutText(text);
    }

}
