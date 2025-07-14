package gr.aueb.cf.insuranceapp.mapper;

import gr.aueb.cf.insuranceapp.dto.*;
import gr.aueb.cf.insuranceapp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

    public CustomerReadOnlyDTO mapToCustomerReadOnlyDTO(Customer customer) {

        CustomerReadOnlyDTO customerReadOnlyDTO = new CustomerReadOnlyDTO();
        customerReadOnlyDTO.setUuid(customer.getUuid());
        customerReadOnlyDTO.setIsActive(customer.getIsActive());

        UserReadOnlyDTO userReadOnlyDTO = new UserReadOnlyDTO();
        userReadOnlyDTO.setFirstname(customer.getUser().getFirstname());
        userReadOnlyDTO.setLastname(customer.getUser().getLastname());
        userReadOnlyDTO.setAfm(customer.getUser().getAfm());
        customerReadOnlyDTO.setUser(userReadOnlyDTO);

        PersonalInfoReadOnlyDTO personalInfoReadOnlyDTO = new PersonalInfoReadOnlyDTO();
        personalInfoReadOnlyDTO.setIdentityNumber(customer.getPersonalInfo().getIdentityNumber());
        customerReadOnlyDTO.setPersonalInfo(personalInfoReadOnlyDTO);

        return customerReadOnlyDTO;
    }

    public Customer mapToCustomerEntity(CustomerInsertDTO dto) {
        Customer customer = new Customer();
        customer.setIsActive(dto.getIsActive());

        UserInsertDTO userInsertDTO = dto.getUser();
        User user = new User();
        user.setFirstname(userInsertDTO.getFirstname());
        user.setLastname(userInsertDTO.getLastname());
        user.setUsername(userInsertDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        user.setAfm(userInsertDTO.getAfm());
        user.setFatherName(userInsertDTO.getFatherName());
        user.setFatherLastname(userInsertDTO.getFatherLastname());
        user.setMotherName(userInsertDTO.getMotherName());
        user.setMotherLastname(userInsertDTO.getMotherLastname());
        user.setDateOfBirth(userInsertDTO.getDateOfBirth());
        user.setRole(userInsertDTO.getRole());
        user.setIsActive(dto.getIsActive());
        customer.setUser(user);

        PersonalInfoInsertDTO personalInfoInsertDTO = dto.getPersonalInfo();
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setIdentityNumber(personalInfoInsertDTO.getIdentityNumber());
        personalInfo.setPlaceOfBirth(personalInfoInsertDTO.getPlaceOfBirth());
        customer.setPersonalInfo(personalInfo);

        return customer;
    }

    public CarReadOnlyDTO mapToCarReadOnlyDTO(Car car) {
        CarReadOnlyDTO carReadOnlyDTO = new CarReadOnlyDTO();
        carReadOnlyDTO.setPlateNumber(car.getPlateNumber());
        carReadOnlyDTO.setVehicleType(car.getVehicleType());
        carReadOnlyDTO.setCustomerAfm(car.getCustomer().getUser().getAfm());
        return carReadOnlyDTO;
    }

    public Car mapToCarEntity(CarInsertDTO dto, Customer customer) {
        Car car = new Car();
        car.setPlateNumber(dto.getPlateNumber());
        car.setCustomer(customer);
        return car;
    }

    public MotorCycleReadOnlyDTO mapToMotorCycleReadOnlyDTO(Motorcycle motorcycle) {
        MotorCycleReadOnlyDTO motorCycleReadOnlyDTO = new MotorCycleReadOnlyDTO();
        motorCycleReadOnlyDTO.setPlateNumber(motorcycle.getPlateNumber());
        motorCycleReadOnlyDTO.setVehicleType(motorcycle.getVehicleType());
        motorCycleReadOnlyDTO.setCustomerAfm(motorcycle.getCustomer().getUser().getAfm());
        return motorCycleReadOnlyDTO;
    }

    public Motorcycle mapToMotorcycleEntity(MotorCycleInsertDTO dto, Customer customer) {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setPlateNumber(dto.getPlateNumber());
        motorcycle.setCustomer(customer);
        return motorcycle;
    }

    public InsurancePolicyReadOnlyDTO mapToInsurancePolicyReadOnlyDTO(InsurancePolicy insurancePolicy) {
        InsurancePolicyReadOnlyDTO dto = new InsurancePolicyReadOnlyDTO();
        dto.setUuid(insurancePolicy.getUuid());
        dto.setStartDate(insurancePolicy.getStartDate());
        dto.setEndDate(insurancePolicy.getEndDate());
        dto.setInsuranceType(insurancePolicy.getInsuranceType());

        dto.setPlateNumber(insurancePolicy.getVehicle().getPlateNumber());
        dto.setVehicleType(insurancePolicy.getVehicle().getVehicleType());

        Customer customer = insurancePolicy.getVehicle().getCustomer();
        dto.setCustomerAfm(customer.getUser().getAfm());
        dto.setCustomerFirstname(customer.getUser().getFirstname());
        dto.setCustomerLastname(customer.getUser().getLastname());

        return dto;
    }

    public InsurancePolicy mapToInsurancePolicyEntity(InsurancePolicyInsertDTO dto, Vehicle vehicle) {
        InsurancePolicy insurancePolicy = new InsurancePolicy();
        insurancePolicy.setStartDate(dto.getStartDate());
        insurancePolicy.setEndDate(dto.getStartDate().plusMonths(6));
        insurancePolicy.setInsuranceType(dto.getInsuranceType());
        insurancePolicy.setVehicle(vehicle);
        return insurancePolicy;
    }

    public Customer updateCustomerEntity(Customer customer, CustomerInsertDTO dto) {
        customer.setIsActive(dto.getIsActive());

        UserInsertDTO userDto = dto.getUser();
        User user = customer.getUser();

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setAfm(userDto.getAfm());
        user.setFatherName(userDto.getFatherName());
        user.setFatherLastname(userDto.getFatherLastname());
        user.setMotherName(userDto.getMotherName());
        user.setMotherLastname(userDto.getMotherLastname());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setRole(userDto.getRole());
        user.setIsActive(dto.getIsActive());

        PersonalInfoInsertDTO personalInfoDto = dto.getPersonalInfo();
        PersonalInfo personalInfo = customer.getPersonalInfo();

        personalInfo.setIdentityNumber(personalInfoDto.getIdentityNumber());
        personalInfo.setPlaceOfBirth(personalInfoDto.getPlaceOfBirth());

        return customer;
    }

    public InsurancePolicy updateInsurancePolicyDate(InsurancePolicy insurancePolicy, LocalDate newStartDate) {
        insurancePolicy.setStartDate(newStartDate);
        insurancePolicy.setEndDate(newStartDate.plusMonths(6));
        return insurancePolicy;
    }
}
