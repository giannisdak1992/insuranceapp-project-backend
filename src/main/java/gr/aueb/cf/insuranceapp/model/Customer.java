package gr.aueb.cf.insuranceapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "customers")

public class Customer extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_info_id")
    private PersonalInfo personalInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new HashSet<>();

    public Set<Vehicle> getAllVehicles() {
        return Collections.unmodifiableSet(vehicles);
    }

    public void SetVehicles (Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicles == null) vehicles = new HashSet<>();
        vehicles.add(vehicle);
        vehicle.setCustomer(this);
    }

    public void removeVehicle (Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setCustomer(null);

    }

    public void initializeUUID() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }

    @PrePersist
    public void onCreate() {
        initializeUUID();
    }
}
