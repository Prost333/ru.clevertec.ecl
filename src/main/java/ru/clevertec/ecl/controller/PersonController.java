package ru.clevertec.ecl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @Operation(
            method = "GET",
            summary = "Get all persons",
            description = "Get all persons",
            parameters = {
                    @Parameter(
                            name = "page",
                            schema = @Schema(implementation = Integer.class),
                            description = "Page number"
                    ),
                    @Parameter(
                            name = "pageSize",
                            schema = @Schema(implementation = Integer.class),
                            description = "Page size"
                    )
            },
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePage.class)
                    )
            )
    )
    @GetMapping("/getAll")
    public ResponsePage<PersonRes> personList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Page<PersonRes> personPage = personService.findAll(page, pageSize);
        return new ResponsePage<>(personPage);
    }

    @Operation(
            method = "POST",
            summary = "Save a person",
            description = "Save a person",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonReq.class)
                    )
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonRes.class)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<PersonRes> save(@RequestBody PersonReq personReq) {
        return ResponseEntity.ok(personService.save(personReq));
    }

    @Operation(
            method = "GET",
            summary = "Find a person by UUID",
            description = "Find a person by UUID",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(implementation = UUID.class),
                    description = "UUID of the person"
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonRes.class)
                    )
            )
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<PersonRes> findByUUId(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(personService.findByUUID(uuid));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete a person",
            description = "Delete a person",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(implementation = UUID.class),
                    description = "UUID of the person"
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "204"
            )
    )
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        personService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            method = "GET",
            summary = "Find persons who live in a house",
            description = "Find persons who live in a house",
            parameters = @Parameter(
                    name = "houseId",
                    schema = @Schema(implementation = UUID.class),
                    description = "UUID of the house"
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonRes.class))
                    )
            )
    )
    @GetMapping("/livesInHouse/")
    public ResponseEntity<List<PersonRes>> findPersonWhoLivesInHouse(@RequestParam(name = "houseId") UUID houseId) {
        return ResponseEntity.ok(personService.findPersonWhoLivesInHouse(houseId));
    }

    @Operation(
            method = "PUT",
            summary = "Update a person",
            description = "Update a person",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(implementation = UUID.class),
                    description = "UUID of the person"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonReq.class)
                    )
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonRes.class)
                    )
            )
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<PersonRes> update(@PathVariable(name = "uuid") UUID uuid,
                                            @RequestBody PersonReq personReq) {
        return ResponseEntity.ok(personService.update(uuid, personReq));
    }

    @Operation(
            method = "GET",
            summary = "Find persons by text",
            description = "Find persons by text",
            parameters = @Parameter(
                    name = "text",
                    schema = @Schema(implementation = String.class),
                    description = "Text to search"
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonRes.class))
                    )
            )
    )
    @GetMapping("/search/{text}")
    public ResponseEntity<List<PersonRes>> findAbsolutText(@PathVariable(name = "text") String text) {
        return ResponseEntity.ok(personService.findAbsolutText(text));
    }

    @Operation(
            method = "GET",
            summary = "Find persons who owned a house at some time",
            description = "Find persons who owned a house at some time",
            parameters = {
                    @Parameter(
                            name = "uuid",
                            schema = @Schema(implementation = UUID.class),
                            description = "UUID of the house"
                    ),
                    @Parameter(
                            name = "page",
                            schema = @Schema(implementation = Pageable.class),
                            description = "Details of the page"
                    )
            },
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            )
    )
    @GetMapping("/SomeTimeOwnPerson/{uuid}")
    public ResponseEntity findPersonWhichSomeTimeOwnHouse(@PathVariable(name = "uuid") UUID uuid, @PageableDefault(10) Pageable pageable) {
        Page page = personService.findByPersonByHistory(uuid, PersonType.OWNER, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
            method = "GET",
            summary = "Find persons who were tenants in a house at some time",
            description = "Find persons who were tenants in a house at some time",
            parameters = {
                    @Parameter(
                            name = "uuid",
                            schema = @Schema(implementation = UUID.class),
                            description = "UUID of the house"
                    ),
                    @Parameter(
                            name = "page",
                            schema = @Schema(implementation = Pageable.class),
                            description = "Details of the page"
                    )
            },
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            )
    )
    @GetMapping("/SomeTimeTenantPerson/{uuid}")
    public ResponseEntity findPersonWhichSomeTimeTenantHouse(@PathVariable(name = "uuid") UUID uuid, @PageableDefault(10) Pageable pageable) {
        Page page = personService.findByPersonByHistory(uuid, PersonType.TENANT, pageable);
        return ResponseEntity.ok(page);
    }


}
