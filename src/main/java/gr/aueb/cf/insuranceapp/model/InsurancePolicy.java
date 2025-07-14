package gr.aueb.cf.insuranceapp.model;

import gr.aueb.cf.insuranceapp.core.enums.InsuranceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "insurance_policies")

public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String uuid;


    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;



    @PrePersist
    private void initializeFields() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        if (this.startDate != null && this.endDate == null) {
            this.endDate = this.startDate.plusMonths(6);
        }
    }

}
