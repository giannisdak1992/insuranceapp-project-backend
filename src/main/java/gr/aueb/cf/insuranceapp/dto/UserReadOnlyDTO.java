package gr.aueb.cf.insuranceapp.dto;


import gr.aueb.cf.insuranceapp.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String afm;
    private String fatherName;
    private String fatherLastname;
    private String motherName;
    private String motherLastname;
    private LocalDate dateOfBirth;
    private Role role;

}
