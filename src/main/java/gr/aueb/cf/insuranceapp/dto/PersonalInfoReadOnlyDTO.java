package gr.aueb.cf.insuranceapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonalInfoReadOnlyDTO {
    private String placeOfBirth;
    private String identityNumber;
}