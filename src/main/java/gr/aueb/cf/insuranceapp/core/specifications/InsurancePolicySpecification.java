package gr.aueb.cf.insuranceapp.core.specifications;

import gr.aueb.cf.insuranceapp.core.filters.InsurancePolicyFilters;
import gr.aueb.cf.insuranceapp.model.InsurancePolicy;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InsurancePolicySpecification {

    private InsurancePolicySpecification() {
    }

    public static Specification<InsurancePolicy> getSpecsFromFilters(InsurancePolicyFilters filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.getFromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), filters.getFromDate()));
            }

            if (filters.getToDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), filters.getToDate()));
            }

            if (filters.getInsuranceType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("insuranceType"), filters.getInsuranceType()));
            }

            if (filters.getVehicleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicle").get("id"), filters.getVehicleId()));
            }

            if (filters.getUuid() != null && !filters.getUuid().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("uuid"), filters.getUuid()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
