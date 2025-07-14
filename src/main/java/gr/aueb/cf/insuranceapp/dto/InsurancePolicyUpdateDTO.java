package gr.aueb.cf.insuranceapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsurancePolicyUpdateDTO {

    @NotNull(message = "starting date is required")
    private LocalDate newStartDate;

}