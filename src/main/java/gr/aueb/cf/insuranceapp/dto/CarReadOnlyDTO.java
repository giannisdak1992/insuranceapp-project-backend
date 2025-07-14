package gr.aueb.cf.insuranceapp.dto;

import gr.aueb.cf.insuranceapp.core.enums.VehicleType;  // <- Χρειάζεται αυτό το import

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarReadOnlyDTO {

    private String plateNumber;
    private VehicleType vehicleType;
    private String customerAfm;
}
