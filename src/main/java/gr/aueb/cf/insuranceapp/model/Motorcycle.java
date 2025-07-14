
package gr.aueb.cf.insuranceapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import gr.aueb.cf.insuranceapp.core.enums.VehicleType;

@Entity
@Table(name = "motorcycles")
public class Motorcycle extends Vehicle {

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.MOTORCYCLE;
    }
}