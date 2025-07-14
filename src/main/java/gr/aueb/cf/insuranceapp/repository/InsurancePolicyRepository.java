package gr.aueb.cf.insuranceapp.repository;

import gr.aueb.cf.insuranceapp.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long>, JpaSpecificationExecutor<InsurancePolicy> {

    Optional<InsurancePolicy> findByUuid(String uuid);

    List<InsurancePolicy> findByVehicleId(Long vehicleId);
}
