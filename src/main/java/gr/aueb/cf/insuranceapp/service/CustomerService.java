package gr.aueb.cf.insuranceapp.service;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.insuranceapp.core.filters.CustomerFilters;
import gr.aueb.cf.insuranceapp.core.filters.Paginated;
import gr.aueb.cf.insuranceapp.core.specifications.CustomerSpecification;
import gr.aueb.cf.insuranceapp.dto.CustomerInsertDTO;
import gr.aueb.cf.insuranceapp.dto.CustomerReadOnlyDTO;
import gr.aueb.cf.insuranceapp.mapper.Mapper;
import gr.aueb.cf.insuranceapp.model.Customer;
import gr.aueb.cf.insuranceapp.repository.CustomerRepository;
import gr.aueb.cf.insuranceapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;


    @Transactional(rollbackOn = Exception.class)
    public CustomerReadOnlyDTO saveCustomer(CustomerInsertDTO customerInsertDTO)
        throws AppObjectAlreadyExists, AppObjectInvalidArgumentException {

        if (userRepository.findByAfm(customerInsertDTO.getUser().getAfm()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with afm " + customerInsertDTO.getUser().getAfm() + " already exists.");
        }

        if (userRepository.findByUsername(customerInsertDTO.getUser().getUsername()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with username " + customerInsertDTO.getUser().getUsername() + " already exists.");
        }

        // Saving the customer cascades user & personal info
        Customer customer = mapper.mapToCustomerEntity(customerInsertDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return mapper.mapToCustomerReadOnlyDTO(savedCustomer);
    }

    public CustomerReadOnlyDTO findByUserAfm(String afm) throws AppObjectInvalidArgumentException {
        Customer customer = customerRepository.findByUserAfm(afm)
                .orElseThrow(() -> new AppObjectInvalidArgumentException(
                        "Customer",
                        "Customer not found with Afm: " + afm
                ));

        return mapper.mapToCustomerReadOnlyDTO(customer);
    }

    public CustomerReadOnlyDTO findByUuid(String uuid) throws AppObjectInvalidArgumentException {
        Customer customer = customerRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Customer.", "Customer not found with uuid: " + uuid));
        return mapper.mapToCustomerReadOnlyDTO(customer);
    }

    @Transactional(rollbackOn = Exception.class)
    public CustomerReadOnlyDTO updateCustomer(Long customerId, CustomerInsertDTO customerInsertDTO)
            throws AppObjectInvalidArgumentException {

        //searching for the customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppObjectInvalidArgumentException(
                        "Customer",
                        "Customer not found with id: " + customerId
                ));

        //updating customer
        Customer updatedCustomer = mapper.updateCustomerEntity(customer, customerInsertDTO);

        //saving the updatedCustomer
        Customer savedCustomer = customerRepository.save(updatedCustomer);

        // convert entity to DTO abd return it
        return mapper.mapToCustomerReadOnlyDTO(savedCustomer);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteCustomer(String uuid) throws AppObjectInvalidArgumentException {
        Customer customer = customerRepository.findByUuid(uuid)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Customer", "Customer not found"));

        customerRepository.delete(customer);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteCustomerByAfm(String afm) throws AppObjectInvalidArgumentException {
        Customer customer = customerRepository.findByUserAfm(afm)
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Customer", "Customer not found with AFM: " + afm));

        customerRepository.delete(customer);
    }

    @Transactional
    public Page<CustomerReadOnlyDTO> getPaginatedCustomers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerReadOnlyDTO);
    }

    @Transactional
    public Page<CustomerReadOnlyDTO> getPaginatedSortedCustomers (int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerReadOnlyDTO);
    }

    @Transactional
    public List<CustomerReadOnlyDTO> getCustomersFiltered(CustomerFilters filters) {
        return customerRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToCustomerReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Paginated<CustomerReadOnlyDTO> getCustomersFilteredPaginated(CustomerFilters filters) {
        var filtered = customerRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToCustomerReadOnlyDTO));
    }

    private Specification<Customer> getSpecsFromFilters(CustomerFilters filters) {
        return Specification
                .where(CustomerSpecification.customerStringFieldLike("uuid", filters.getUuid()))
                .and(CustomerSpecification.customerUserAfmIs(filters.getUserAfm()))
                .and(CustomerSpecification.customerIsActive(filters.getIsActive()));
    }

}
