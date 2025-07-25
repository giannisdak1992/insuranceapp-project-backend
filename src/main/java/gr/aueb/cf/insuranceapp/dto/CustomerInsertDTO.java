package gr.aueb.cf.insuranceapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerInsertDTO {

    @NotNull(message = "isActive field is required")
    private Boolean isActive;

    @NotNull(message = "User details are required")
    private UserInsertDTO user;

    @NotNull(message = "Personal Info is required")
    private PersonalInfoInsertDTO personalInfo;
}
