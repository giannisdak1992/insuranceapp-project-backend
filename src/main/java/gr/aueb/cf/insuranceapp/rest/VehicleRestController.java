package gr.aueb.cf.insuranceapp.rest;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.insuranceapp.core.exceptions.ValidationException;
import gr.aueb.cf.insuranceapp.dto.*;
import gr.aueb.cf.insuranceapp.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleRestController {

    private final VehicleService vehicleService;

    @Operation(
            summary = "Get all vehicle plate numbers for a specific customer AFM",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of plate numbers"),
                    @ApiResponse(responseCode = "404", description = "Customer not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("/plates/{afm}")
    public ResponseEntity<List<String>> getVehiclePlatesByCustomerAfm(@PathVariable String afm) throws AppObjectInvalidArgumentException {
        List<String> plates = vehicleService.getPlatesByCustomerAfm(afm);
        return new ResponseEntity<>(plates, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all cars paginated",
            security = @SecurityRequirement(name = "Bearer Authentication"), // that endpoint requires authentication
            responses = { // potential Http responses
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cars Found",
                            content = @Content(
                                    mediaType = "application/json", // return JSON TeacherReadOnlyDTO
                                    schema = @Schema(implementation = CarReadOnlyDTO.class)
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
    @GetMapping("/cars/paginated")
    public ResponseEntity<Page<CarReadOnlyDTO>> getPaginatedCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Page<CarReadOnlyDTO> carsPage = vehicleService.getPaginatedCars(page, size);
        return new ResponseEntity<>(carsPage, HttpStatus.OK);
    }
    @Operation(
            summary = "Register a new car",
            description = "Creates and saves a new car vehicle record",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Car saved successfully",
                            content = @Content(schema = @Schema(implementation = CarReadOnlyDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )

    @PostMapping("/cars")
    public ResponseEntity<CarReadOnlyDTO> saveCar(
            @Valid @RequestBody CarInsertDTO carInsertDTO,
            BindingResult bindingResult
            ) throws AppObjectInvalidArgumentException, ValidationException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CarReadOnlyDTO carReadOnlyDTO = vehicleService.saveCar(carInsertDTO);
        return new ResponseEntity<>(carReadOnlyDTO, HttpStatus.OK);
    }
    @Operation(
            summary = "Register a new motorcycle",
            description = "Creates and saves a new motorcycle vehicle record",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Motorcycle saved successfully",
                            content = @Content(schema = @Schema(implementation = MotorCycleReadOnlyDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )

    @PostMapping("/motorcycles")
    public ResponseEntity<MotorCycleReadOnlyDTO> saveMotorCycle(
            @Valid @RequestBody MotorCycleInsertDTO motorCycleInsertDTO,
            BindingResult bindingResult
            ) throws AppObjectInvalidArgumentException, ValidationException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        MotorCycleReadOnlyDTO motorCycleReadOnlyDTO = vehicleService.saveMotorCycle(motorCycleInsertDTO);
        return new ResponseEntity<>(motorCycleReadOnlyDTO, HttpStatus.OK);
    }
}
