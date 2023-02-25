package com.allane.leasingapi.controller;

import com.allane.leasingapi.dto.Vehicle;
import com.allane.leasingapi.dto.Vehicles;
import com.allane.leasingapi.dto.cruddto.CrudVehicleDto;
import com.allane.leasingapi.service.CRUDOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@RestController
@RequestMapping("/vehicle")
public class VehicleCrudController {

    private final CRUDOperation<Vehicle, CrudVehicleDto, Long, Vehicles> vehicleService;

    public VehicleCrudController(CRUDOperation<Vehicle, CrudVehicleDto, Long, Vehicles> vehicleService) {
        this.vehicleService = vehicleService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Gets Vehicle")
    @GetMapping("/{vehicle-id}")
    public Vehicle get(@PathVariable(value = "vehicle-id", required = false) Long vehicleId) {

        return vehicleService.find(vehicleId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Gets All Vehicles")
    @GetMapping
    public Vehicles getAll() {

        return vehicleService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "Illegal arguments provided",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Creates a Vehicle")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Vehicle create(@Valid @RequestBody CrudVehicleDto crudVehicleDto) {

        return vehicleService.create(crudVehicleDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Deletes the Vehicle")
    @DeleteMapping(value = "/{vehicle-id}")
    public void delete(@PathVariable(value = "vehicle-id") Long vehicleId) {

        vehicleService.delete(vehicleId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Updates the Vehicle")
    @PutMapping(value = "/{vehicle-id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Vehicle update(@PathVariable(value = "vehicle-id") Long vehicleId,
                          @Valid @RequestBody CrudVehicleDto crudVehicleDto) {

        return vehicleService.update(vehicleId, crudVehicleDto);
    }
}
