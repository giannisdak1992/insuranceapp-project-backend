package gr.aueb.cf.insuranceapp.repository;

import gr.aueb.cf.insuranceapp.model.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByUuid(String uuid);
    Optional<Customer> findByUserAfm(String afm);
    Optional<Customer> findByUserUsername(String username);

    @EntityGraph(attributePaths = {"user", "personalInfo"})
    Optional<Customer> findWithUserAndPersonalInfoById(Long id);
}
