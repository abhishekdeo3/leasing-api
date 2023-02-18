package com.allane.leasingapi.controller;

import com.allane.leasingapi.dto.Customer;
import com.allane.leasingapi.dto.Customers;
import com.allane.leasingapi.dto.cruddto.CrudCustomerDto;
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
@RequestMapping("/customer")
public class CustomerCrudController {

    private final CRUDOperation<Customer, CrudCustomerDto, Long, Customers> customerService;

    public CustomerCrudController(CRUDOperation<Customer, CrudCustomerDto, Long, Customers> customerService) {
        this.customerService = customerService;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Gets Customer")
    @GetMapping("/{customer-id}")
    public Customer get(@PathVariable(value = "customer-id", required = false) Long customerId) {

        return customerService.find(customerId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Gets All Customers")
    @GetMapping
    public Customers getAll() {

        return customerService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "Illegal arguments provided",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Creates a Customer")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Customer create(@Valid @RequestBody CrudCustomerDto crudCustomerDto) {

        return customerService.create(crudCustomerDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Deletes the Customer")
    @DeleteMapping(value = "/{customer-id}")
    public void delete(@PathVariable(value = "customer-id") Long customerId) {

        customerService.delete(customerId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE)})
    })
    @Operation(summary = "Updates the Customer")
    @PutMapping(value = "/{customer-id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Customer update(@PathVariable(value = "customer-id") Long customerId,
                           @Valid @RequestBody CrudCustomerDto crudCustomerDto) {

        return customerService.update(customerId, crudCustomerDto);
    }
}
