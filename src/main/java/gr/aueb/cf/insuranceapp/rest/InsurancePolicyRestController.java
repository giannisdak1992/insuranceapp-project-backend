package gr.aueb.cf.insuranceapp.rest;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.insuranceapp.core.filters.InsurancePolicyFilters;
import gr.aueb.cf.insuranceapp.core.filters.Paginated;
import gr.aueb.cf.insuranceapp.dto.InsurancePolicyInsertDTO;
import gr.aueb.cf.insuranceapp.dto.InsurancePolicyReadOnlyDTO;
import gr.aueb.cf.insuranceapp.service.InsurancePolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class InsurancePolicyRestController {

    private final InsurancePolicyService insurancePolicyService;

    @Operation(
            summary = "Create a new insurance policy",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Policy created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )
    @PostMapping("/create")
    public ResponseEntity<InsurancePolicyReadOnlyDTO> createPolicy(
            @Valid @RequestBody InsurancePolicyInsertDTO dto,
            BindingResult bindingResult
    ) throws AppObjectInvalidArgumentException {
        if (bindingResult.hasErrors()) {
            throw new AppObjectInvalidArgumentException("Validation", "Invalid insurance policy input.");
        }

        InsurancePolicyReadOnlyDTO created = insurancePolicyService.saveInsurancePolicy(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get insurance policy by UUID",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Policy found",
                            content = @Content(schema = @Schema(implementation = InsurancePolicyReadOnlyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid UUID", content = @Content)
            }
    )

    @GetMapping("/{uuid}")
    public ResponseEntity<InsurancePolicyReadOnlyDTO> getPolicyByUuid(@PathVariable String uuid)
            throws AppObjectInvalidArgumentException {
        return ResponseEntity.ok(insurancePolicyService.findByUuid(uuid));
    }

    @Operation(
            summary = "Update start date of a policy",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Start date updated",
                            content = @Content(schema = @Schema(implementation = InsurancePolicyReadOnlyDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid date or UUID", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content)
            }
    )

    @PutMapping("/{uuid}/update-start-date")
    public ResponseEntity<InsurancePolicyReadOnlyDTO> updatePolicyStartDate(
            @PathVariable String uuid,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newStartDate
    ) throws AppObjectInvalidArgumentException {
        InsurancePolicyReadOnlyDTO updated = insurancePolicyService.updateInsurancePolicy(uuid, newStartDate);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Get paginated insurance policies",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated policies found",
                            content = @Content(schema = @Schema(implementation = InsurancePolicyReadOnlyDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )

    @GetMapping("/paginated")
    public ResponseEntity<Page<InsurancePolicyReadOnlyDTO>> getPaginatedPolicies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<InsurancePolicyReadOnlyDTO> result = insurancePolicyService.getPaginatedInsurancePolicies(page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get filtered insurance policies",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered policies found"),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )
    @PostMapping("/filtered")
    public ResponseEntity<List<InsurancePolicyReadOnlyDTO>> getFilteredPolicies(
            @Nullable @RequestBody InsurancePolicyFilters filters
    ) {
        if (filters == null) filters = InsurancePolicyFilters.builder().build();
        return ResponseEntity.ok(insurancePolicyService.getInsurancePoliciesFiltered(filters));
    }

    @Operation(
            summary = "Get filtered and paginated insurance policies",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered paginated policies found"),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )
    @PostMapping("/filtered/paginated")
    public ResponseEntity<Paginated<InsurancePolicyReadOnlyDTO>> getFilteredPaginatedPolicies(
            @Nullable @RequestBody InsurancePolicyFilters filters
    ) {
        if (filters == null) filters = InsurancePolicyFilters.builder().build();
        return ResponseEntity.ok(insurancePolicyService.getInsurancePoliciesFilteredPaginated(filters));
    }

    @Operation(
            summary = "Get current customer's insurance policies",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer policies found"),
                    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
            }
    )

    @PostMapping("/customer/self")
    public ResponseEntity<List<InsurancePolicyReadOnlyDTO>> getPoliciesForCurrentCustomer(
            @Nullable @RequestBody InsurancePolicyFilters filters
    ) throws AppObjectInvalidArgumentException {
        if (filters == null) filters = InsurancePolicyFilters.builder().build();
        return ResponseEntity.ok(insurancePolicyService.getInsurancePoliciesForCurrentCustomer(filters));
    }

}
