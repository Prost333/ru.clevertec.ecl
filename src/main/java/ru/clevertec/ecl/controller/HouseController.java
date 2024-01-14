package ru.clevertec.ecl.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.service.HouseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/house")
@AllArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/{uuid}")
    public HouseRes findById(@PathVariable(name = "uuid") UUID uuid) {
        return houseService.findByUUID(uuid);
    }

    @PostMapping
    public HouseRes save(@RequestBody HouseReq houseReq) {
        return houseService.save(houseReq);
    }

    @GetMapping
    public List<HouseRes> findAll(@RequestParam(defaultValue = "1", name = "page") int page,
                                  @RequestParam(defaultValue = "10", name = "pageSize") int pageSize) {
        return houseService.findAll(page, pageSize);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable(name = "uuid") UUID uuid) {
        houseService.delete(uuid);
    }

    @PutMapping("/{uuid}")
    public HouseRes update(@PathVariable(name = "uuid") UUID uuid,
                           @RequestBody HouseReq houseReq) {
        return houseService.update(uuid, houseReq);
    }

    @GetMapping("/lives/{uuid}")
    public List<HouseRes> findHousesWhichOwnPerson(@PathVariable(name = "uuid") UUID uuid) {
        return houseService.findOwners(uuid);
    }

    @GetMapping("/search/{text}")
    public List<HouseRes> findAbsolutText(@PathVariable(name = "text") String text) {

        return houseService.findAbsolutText(text);
    }

    @PutMapping("Buy/{uuid}/{personId}")
    public void buyHouse(@PathVariable(name = "uuid") UUID uuid, @PathVariable(name = "personId") UUID personId){
        houseService.buyHouse(uuid, personId);
    }
}
