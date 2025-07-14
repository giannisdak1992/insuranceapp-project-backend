package gr.aueb.cf.insuranceapp.dto;

import gr.aueb.cf.insuranceapp.core.enums.VehicleType;  // <-- Πρόσθεσε αυτό

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MotorCycleReadOnlyDTO {
    private String plateNumber;
    private VehicleType vehicleType;
    private String customerAfm;
}
