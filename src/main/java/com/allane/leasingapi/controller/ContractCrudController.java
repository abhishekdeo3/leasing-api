package com.allane.leasingapi.controller;

import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.dto.Contracts;
import com.allane.leasingapi.dto.cruddto.CrudContractDto;
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
@RequestMapping("/contract")
public class ContractCrudController {
    private final CRUDOperation<Contract, CrudContractDto, Long, Contracts> contractService;

    public ContractCrudController(CRUDOperation<Contract, CrudContractDto, Long, Contracts> contractService) {
        this.contractService = contractService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Gets Contract")
    @GetMapping("/{contract-number}")
    public Contract get(@PathVariable(value = "contract-number", required = false) Long contractNumber) {

        return contractService.find(contractNumber);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Gets All Contract")
    @GetMapping
    public Contracts getAll() {

        return contractService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "Illegal arguments provided",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Creates a Contract")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Contract create(@Valid @RequestBody CrudContractDto crudContractDto) {

        return contractService.create(crudContractDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Deletes the Contract")
    @DeleteMapping(value = "/{contract-number}")
    public void delete(@PathVariable(value = "contract-number") Long contractNumber) {

        contractService.delete(contractNumber);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Updates the Contract")
    @PutMapping(value = "/{contract-number}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Contract update(@PathVariable(value = "contract-number") Long contractNumber,
                       @Valid @RequestBody CrudContractDto crudContractDto) {

        return contractService.update(contractNumber, crudContractDto);
    }
}
