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


}
