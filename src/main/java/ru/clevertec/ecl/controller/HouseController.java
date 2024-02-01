package ru.clevertec.ecl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "House", description = "The House API")
public class HouseController {

    private final HouseService houseService;

    @Operation(
            method = "GET",
            summary = "Get house by UUID",
            description = "Get house by accept UUID as path variable",
            parameters = @Parameter(
                    name = "UUID",
                    schema = @Schema(
                            oneOf = UUID.class
                    ),
                    required = true,
                    description = "Universal unique identifier of object"
            ),
            responses = {
                    @ApiResponse(
                            description = "Get house",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<HouseRes> findById(@PathVariable(name = "uuid") UUID uuid) {
        return ResponseEntity.ok(houseService.findByUUID(uuid));
    }

    @Operation(
            method = "POST",
            summary = "Save house",
            description = "Save house",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HouseReq.class)
                    )
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HouseRes.class)
                    )
            )
    )
    @PostMapping
    public HouseRes save(@RequestBody HouseReq houseReq) {
        return houseService.save(houseReq);
    }

    @Operation(
            method = "GET",
            summary = "Get houses",
            description = "Get houses",
            parameters = @Parameter(
                    name = "Pageable",
                    schema = @Schema(
                            implementation = Pageable.class
                    ),
                    description = "Pageable object for pagination with size, page and sort parameters"
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            })
    @GetMapping
    public ResponsePage<HouseRes> findAll(@RequestParam(defaultValue = "0", name = "page") int page,
                                          @RequestParam(defaultValue = "10", name = "pageSize") int pageSize) {
        Page<HouseRes> housePage = houseService.findAll(page, pageSize);
        return new ResponsePage<>(housePage);
    }

    @Operation(
            method = "DELETE",
            summary = "delete house",
            description = "delete house",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(implementation = UUID.class)),
            responses = @ApiResponse(description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    )))
    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable(name = "uuid") UUID uuid) {
        houseService.delete(uuid);
    }

    @Operation(
            method = "PUT",
            summary = "Update house",
            description = "Update house",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(implementation = UUID.class)
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HouseReq.class)
                    )
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HouseRes.class)
                    )
            )
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<HouseRes> update(@PathVariable(name = "uuid") UUID uuid,
                                           @RequestBody HouseReq houseReq) {
        return ResponseEntity.ok(houseService.update(uuid, houseReq));
    }

    @Operation(
            method = "GET",
            summary = "Find houses owned by a person",
            description = "Find houses owned by a person",
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
                            array = @ArraySchema(schema = @Schema(implementation = HouseRes.class))
                    )
            )
    )
    @GetMapping("/lives/{uuid}")
    public ResponseEntity<List<HouseRes>> findHousesWhichOwnPerson(@PathVariable(name = "uuid") UUID uuid) {
        return ResponseEntity.ok(houseService.findOwners(uuid));
    }

    @Operation(
            method = "GET",
            summary = "Find houses by text",
            description = "Find houses by text",
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
                            array = @ArraySchema(schema = @Schema(implementation = HouseRes.class))
                    )
            )
    )
    @GetMapping("/search/{text}")
    public ResponseEntity<List<HouseRes>> findAbsolutText(@PathVariable(name = "text") String text) {

        return ResponseEntity.ok(houseService.findAbsolutText(text));
    }

    @Operation(
            method = "PUT",
            summary = "Buy a house",
            description = "Buy a house",
            parameters = {
                    @Parameter(
                            name = "uuid",
                            schema = @Schema(implementation = UUID.class),
                            description = "UUID of the house"
                    ),
                    @Parameter(
                            name = "personId",
                            schema = @Schema(implementation = UUID.class),
                            description = "UUID of the person"
                    )
            },
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200"
            )
    )
    @PutMapping("Buy/{uuid}/{personId}")
    public void buyHouse(@PathVariable(name = "uuid") UUID uuid, @PathVariable(name = "personId") UUID personId) {
        houseService.buyHouse(uuid, personId);
    }

    @Operation(
            method = "GET",
            summary = "Find houses where a person lived at some time",
            description = "Find houses where a person lived at some time",
            parameters = {
                    @Parameter(
                            name = "personID",
                            schema = @Schema(implementation = UUID.class),
                            description = "UUID of the person"
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
    @GetMapping("/SomeTimeLivesPerson/{personID}")
    public ResponseEntity<Page<HouseRes>> findHousesWhichSomeTimeLivesPerson(@PathVariable(name = "personID") UUID personId, @PageableDefault(10) Pageable pageable) {
        Page page = houseService.findHousesWhichSomeTimeLivesPerson(personId, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
            method = "GET",
            summary = "Find houses that a person owned at some time",
            description = "Find houses that a person owned at some time",
            parameters = {
                    @Parameter(
                            name = "personID",
                            schema = @Schema(implementation = UUID.class),
                            description = "UUID of the person"
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
                            array = @ArraySchema(schema = @Schema(implementation = HouseRes.class))
                    )
            )
    )
    @GetMapping("/SomeTimeOwnPerson/{personID}")
    public ResponseEntity<Page<HouseRes>> findHousesWhichSomeTimeOwnPerson(@PathVariable(name = "personID") UUID personId, @PageableDefault(10) Pageable pageable) {
        Page page = houseService.findHousesWhichSomeTimeOwnPerson(personId, pageable);
        return ResponseEntity.ok(page);
    }
}
