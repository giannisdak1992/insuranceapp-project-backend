package gr.aueb.cf.insuranceapp.dto;

import gr.aueb.cf.insuranceapp.core.enums.InsuranceType;

import java.time.LocalDate;

import gr.aueb.cf.insuranceapp.core.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsurancePolicyReadOnlyDTO {
    private String uuid;
    private LocalDate startDate;
    private LocalDate endDate;
    private InsuranceType insuranceType;

    private String plateNumber;
    private VehicleType vehicleType;
    private String customerAfm;
    private String customerFirstname;
    private String customerLastname;
}
