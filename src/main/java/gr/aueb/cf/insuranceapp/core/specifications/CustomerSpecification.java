package gr.aueb.cf.insuranceapp.core.specifications;

import gr.aueb.cf.insuranceapp.model.Customer;
import gr.aueb.cf.insuranceapp.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {
    private CustomerSpecification () {

    }

    public static Specification<Customer> customerUserAfmIs(String afm) {
        return ((root,query,criteriaBuilder) -> {
            if (afm == null || afm.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("afm"), afm);
        });
    }

    public static Specification<Customer> customerIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("isActive"), isActive);
        });
    }


    public static Specification<Customer> customerStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        });
    }
}
