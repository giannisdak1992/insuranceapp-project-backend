package gr.aueb.cf.insuranceapp.rest;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.insuranceapp.core.exceptions.ValidationException;
import gr.aueb.cf.insuranceapp.dto.CarInsertDTO;
import gr.aueb.cf.insuranceapp.dto.CarReadOnlyDTO;
import gr.aueb.cf.insuranceapp.dto.MotorCycleInsertDTO;
import gr.aueb.cf.insuranceapp.dto.MotorCycleReadOnlyDTO;
import gr.aueb.cf.insuranceapp.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleRestController {

    private final VehicleService vehicleService;
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
