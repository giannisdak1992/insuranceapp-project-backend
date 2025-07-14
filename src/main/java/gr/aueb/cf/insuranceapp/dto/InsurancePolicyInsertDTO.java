package gr.aueb.cf.insuranceapp.dto;

import gr.aueb.cf.insuranceapp.core.enums.InsuranceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InsurancePolicyInsertDTO {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Choose third party or full coverage type")
    private InsuranceType insuranceType;

    @NotEmpty(message = "Plate number is required")
    private String plateNumber;

    @NotEmpty(message = "Customer's Afm is required")
    private String customerAfm;
}
