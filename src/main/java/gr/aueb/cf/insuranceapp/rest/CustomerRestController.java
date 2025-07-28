package gr.aueb.cf.insuranceapp.rest;


import gr.aueb.cf.insuranceapp.core.exceptions.*;
import gr.aueb.cf.insuranceapp.core.filters.CustomerFilters;
import gr.aueb.cf.insuranceapp.core.filters.Paginated;
import gr.aueb.cf.insuranceapp.dto.CustomerInsertDTO;
import gr.aueb.cf.insuranceapp.dto.CustomerReadOnlyDTO;
import gr.aueb.cf.insuranceapp.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerService customerService;

    @Operation(
            summary = "Create a new customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Customer already exists", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )
    @PostMapping("/customers/save")
    public ResponseEntity<CustomerReadOnlyDTO> saveCustomer(
            @Valid @RequestBody CustomerInsertDTO customerInsertDTO,
            BindingResult bindingResult
    ) throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.saveCustomer(customerInsertDTO);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);

    }

    @Operation(
            summary = "Get all customers paginated",
            security = @SecurityRequirement(name = "Bearer Authentication"), // that endpoint requires authentication
            responses = { // potential Http responses
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customers Found",
                            content = @Content(
                                    mediaType = "application/json", // return JSON TeacherReadOnlyDTO
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content
                    )
            }
    )
    @GetMapping("/customers/paginated")
    public ResponseEntity<Page<CustomerReadOnlyDTO>> getPaginatedCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Page<CustomerReadOnlyDTO> customersPage = customerService.getPaginatedCustomers(page, size);
        return new ResponseEntity<>(customersPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered list of customers",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customers found"),
                    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )
    @PostMapping("/customers/filtered")
    public ResponseEntity<List<CustomerReadOnlyDTO>> getFilteredCustomers(@Nullable @RequestBody CustomerFilters filters)
        throws AppObjectNotAuthorizedException {
        if (filters == null) filters = CustomerFilters.builder().build(); // instance with null fields

        return ResponseEntity.ok(customerService.getCustomersFiltered(filters));
    }
    @Operation(
            summary = "Get filtered and paginated list of customers",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated customers found"),
                    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )
    @PostMapping("/customers/filtered/paginated")
    public ResponseEntity<Paginated<CustomerReadOnlyDTO>> getCustomersFilteredPaginated(@Nullable @RequestBody CustomerFilters filters)
        throws AppObjectNotAuthorizedException {

        if (filters == null) filters = CustomerFilters.builder().build();

        return ResponseEntity.ok(customerService.getCustomersFilteredPaginated(filters));
    }

    @Operation(
            summary = "Get customer by AFM",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer found",
                            content = @Content(schema = @Schema(implementation = CustomerReadOnlyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid AFM", content = @Content)
            }
    )

    @GetMapping("/customers/afm/{afm}")
    public ResponseEntity<CustomerReadOnlyDTO> getCustomerByAfm(@PathVariable String afm)
            throws AppObjectInvalidArgumentException {
        return ResponseEntity.ok(customerService.findByUserAfm(afm));
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Retrieves customer details by their unique ID",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer found and returned successfully"),
                    @ApiResponse(responseCode = "404", description = "Customer with the specified ID was not found", content = @Content)
            }
    )


    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerReadOnlyDTO> getCustomerById(@PathVariable Long id) throws AppObjectInvalidArgumentException {
        CustomerReadOnlyDTO customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @Operation(
            summary = "Update existing customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
            }
    )


    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerReadOnlyDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerInsertDTO customerInsertDTO,
            BindingResult bindingResult
    ) throws AppObjectInvalidArgumentException, ValidationException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(customerService.updateCustomer(id, customerInsertDTO));
    }


}
