package gr.aueb.cf.insuranceapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarInsertDTO {
    @NotEmpty(message = "Plate number is required")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "Car's plate must consists of 3 letters followed by 3 digits")
    private String plateNumber;

    @NotNull(message = "Customer's Afm is required")
    private String afm;
}
