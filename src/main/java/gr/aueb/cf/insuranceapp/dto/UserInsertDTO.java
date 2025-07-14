package gr.aueb.cf.insuranceapp.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import gr.aueb.cf.insuranceapp.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UserInsertDTO {
    @NotEmpty(message = "First name is required")
    private String firstname;

    @NotEmpty(message = "Last name is required")
    private String lastname;


    @NotEmpty(message = "Username (email) is required")
    @Email(message = "Invalid username")
    private String username;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[@#$!%&*]).{8,}$",
            message = "Invalid Password")
    private String password;

    @NotEmpty(message = "VAT number is required")
    @Pattern(regexp = "\\d{9}", message = "Afm must be a 9-digit number")
    private String afm;

    @NotEmpty(message = "Father's name is required")
    private String fatherName;

    @NotEmpty(message = "Father's last name is required")
    private String fatherLastname;

    @NotEmpty(message = "Mother's name is required")
    private String motherName;

    @NotEmpty(message = "Mother's last name is required")
    private String motherLastname;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;


    @NotNull(message = "Role is required")
    private Role role;
}
