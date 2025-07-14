package gr.aueb.cf.insuranceapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "personal_information")


public class PersonalInfo extends AbstractEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "identity_number")
    private String identityNumber;

    @Column(name = "place_of_birth")
    private String placeOfBirth;


}
