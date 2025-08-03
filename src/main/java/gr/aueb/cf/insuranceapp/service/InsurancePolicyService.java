package gr.aueb.cf.insuranceapp.service;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.insuranceapp.core.filters.CustomerFilters;
import gr.aueb.cf.insuranceapp.core.filters.InsurancePolicyFilters;
import gr.aueb.cf.insuranceapp.core.filters.Paginated;
import gr.aueb.cf.insuranceapp.dto.CustomerReadOnlyDTO;
import gr.aueb.cf.insuranceapp.dto.InsurancePolicyInsertDTO;
import gr.aueb.cf.insuranceapp.dto.InsurancePolicyReadOnlyDTO;
import gr.aueb.cf.insuranceapp.mapper.Mapper;
import gr.aueb.cf.insuranceapp.model.Customer;
import gr.aueb.cf.insuranceapp.model.InsurancePolicy;
import gr.aueb.cf.insuranceapp.model.Vehicle;
import gr.aueb.cf.insuranceapp.repository.CustomerRepository;
import gr.aueb.cf.insuranceapp.repository.InsurancePolicyRepository;
import gr.aueb.cf.insuranceapp.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static gr.aueb.cf.insuranceapp.core.specifications.InsurancePolicySpecification.getSpecsFromFilters;

@Service
@RequiredArgsConstructor
public class InsurancePolicyService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public InsurancePolicyReadOnlyDTO saveInsurancePolicy(InsurancePolicyInsertDTO dto)
            throws AppObjectInvalidArgumentException {

        Vehicle vehicle = vehicleRepository.findByPlateNumber(dto.getPlateNumber())
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Vehicle", "Vehicle not found with plate number: " + dto.getPlateNumber()));

        if (!vehicle.getCustomer().getUser().getAfm().equals(dto.getCustomerAfm())) {
            throw new AppObjectInvalidArgumentException("Customer", "Customer VAT does not match vehicle's owner.");
        }

        InsurancePolicy insurancePolicy = mapper.mapToInsurancePolicyEntity(dto, vehicle);
        InsurancePolicy savedPolicy = insurancePolicyRepository.save(insurancePolicy);

        return mapper.mapToInsurancePolicyReadOnlyDTO(savedPolicy);
    }

    public InsurancePolicyReadOnlyDTO findByUuid(String uuid) throws AppObjectInvalidArgumentException {
        InsurancePolicy policy = insurancePolicyRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Policy", "Insurance policy not found with uuid: " + uuid));

        return mapper.mapToInsurancePolicyReadOnlyDTO(policy);
    }

    @Transactional(rollbackOn = Exception.class)
    public InsurancePolicyReadOnlyDTO updateInsurancePolicy(String uuid, LocalDate newStartDate)
            throws AppObjectInvalidArgumentException {

        InsurancePolicy insurancePolicy = insurancePolicyRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Policy", "Insurance policy not found with uuid: " + uuid));


        LocalDate today = LocalDate.now();

        // checking if the new start date is in the past
        if (newStartDate.isBefore(today)) {
            throw new AppObjectInvalidArgumentException("Invalid Date", "New starting date cannot be in the past.");
        }

        // checking if the new start date is after the current end date
        if (!newStartDate.isBefore(insurancePolicy.getEndDate())) {
            throw new AppObjectInvalidArgumentException("Invalid Date", "New starting date must be after the current end date.");
        }

        InsurancePolicy updated = mapper.updateInsurancePolicyDate(insurancePolicy, newStartDate);
        InsurancePolicy saved = insurancePolicyRepository.save(updated);

        return mapper.mapToInsurancePolicyReadOnlyDTO(saved);
    }

    @Transactional
    public Page<InsurancePolicyReadOnlyDTO> getPaginatedInsurancePolicies(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return insurancePolicyRepository.findAll(pageable)
                .map(mapper::mapToInsurancePolicyReadOnlyDTO);
    }

    public List<InsurancePolicyReadOnlyDTO> getInsurancePoliciesFiltered(InsurancePolicyFilters filters) {
        return insurancePolicyRepository.findAll(getSpecsFromFilters(filters))
                .stream()
                .map(mapper::mapToInsurancePolicyReadOnlyDTO)
                .collect(Collectors.toList());
    }


    public List<InsurancePolicyReadOnlyDTO> getInsurancePoliciesForCurrentCustomer(InsurancePolicyFilters filters) throws AppObjectInvalidArgumentException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer customer = customerRepository.findByUserUsername(currentUsername)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Customer", "Customer not found for user: " + currentUsername));

        Specification<InsurancePolicy> spec = getSpecsFromFilters(filters)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vehicle").get("customer").get("id"), customer.getId()));

        return insurancePolicyRepository.findAll(spec)
                .stream()
                .map(mapper::mapToInsurancePolicyReadOnlyDTO)
                .collect(Collectors.toList());
    }



    @Transactional
    public Paginated<InsurancePolicyReadOnlyDTO> getInsurancePoliciesFilteredPaginated(InsurancePolicyFilters filters) {
        var filtered = insurancePolicyRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToInsurancePolicyReadOnlyDTO));
    }
}
