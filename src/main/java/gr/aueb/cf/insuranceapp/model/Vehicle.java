package gr.aueb.cf.insuranceapp.model;

import gr.aueb.cf.insuranceapp.core.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Table(name = "vehicles")
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InsurancePolicy> insurancePolicies = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Μεθοδος abstract που θα αναγκάζει τις υποκλάσεις να επιστρέφουν το σωστό VehicleType
    public abstract VehicleType getVehicleType();

    public Set<InsurancePolicy> getAllInsurancePolicies() {
        return Collections.unmodifiableSet(insurancePolicies);
    }

    public void addInsurancePolicy(InsurancePolicy policy) {
        insurancePolicies.add(policy);
        policy.setVehicle(this);
    }

    public void removeInsurancePolicy(InsurancePolicy policy) {
        insurancePolicies.remove(policy);
        policy.setVehicle(null);
    }
}
