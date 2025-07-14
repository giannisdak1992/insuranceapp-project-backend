package gr.aueb.cf.insuranceapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PersonalInfoInsertDTO {


    @NotEmpty(message = "Identity number is required")
    private String identityNumber;

    @NotEmpty(message = "Place of birth is required")
    private String placeOfBirth;
}
