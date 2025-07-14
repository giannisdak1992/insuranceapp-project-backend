package gr.aueb.cf.insuranceapp.repository;

import gr.aueb.cf.insuranceapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByAfm(String afm);
    Optional<User> findByUsername(String username);
}
