package gr.aueb.cf.insuranceapp.repository;

import gr.aueb.cf.insuranceapp.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    Optional<Vehicle> findByPlateNumber(String plateNumber);
    List<Vehicle> findByCustomerId(Long customerId);
    List<Vehicle> findByCustomerUserAfm(String afm);
}
