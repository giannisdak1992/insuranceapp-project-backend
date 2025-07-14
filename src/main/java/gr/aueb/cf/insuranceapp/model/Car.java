package gr.aueb.cf.insuranceapp.model;

import gr.aueb.cf.insuranceapp.core.enums.VehicleType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car extends Vehicle {
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }

}
