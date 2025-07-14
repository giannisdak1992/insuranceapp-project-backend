package gr.aueb.cf.insuranceapp.service;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.insuranceapp.dto.CarInsertDTO;
import gr.aueb.cf.insuranceapp.dto.CarReadOnlyDTO;
import gr.aueb.cf.insuranceapp.dto.MotorCycleInsertDTO;
import gr.aueb.cf.insuranceapp.dto.MotorCycleReadOnlyDTO;
import gr.aueb.cf.insuranceapp.mapper.Mapper;
import gr.aueb.cf.insuranceapp.model.Car;
import gr.aueb.cf.insuranceapp.model.Customer;
import gr.aueb.cf.insuranceapp.model.Motorcycle;
import gr.aueb.cf.insuranceapp.model.Vehicle;
import gr.aueb.cf.insuranceapp.repository.CustomerRepository;
import gr.aueb.cf.insuranceapp.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public CarReadOnlyDTO saveCar(CarInsertDTO carInsertDTO) throws AppObjectInvalidArgumentException {

        // Checking if the vehicle exists
        if (vehicleRepository.findByPlateNumber(carInsertDTO.getPlateNumber()).isPresent()) {
            throw new AppObjectInvalidArgumentException("Vehicle.", "Vehicle with plate number " + carInsertDTO.getPlateNumber() + " already exists.");
        }

        // finding the customer with vat

        Customer customer = customerRepository.findByUserAfm(carInsertDTO.getAfm())
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Customer.", "Customer not found with Afm: " + carInsertDTO.getAfm()));


        //mapping to Car Entity
        Car car = mapper.mapToCarEntity(carInsertDTO, customer);

        // saving the vehicle
        Car savedCar = vehicleRepository.save(car);

        return mapper.mapToCarReadOnlyDTO(savedCar);


    }


    @Transactional(rollbackOn = Exception.class)
    public MotorCycleReadOnlyDTO saveMotorCycle(MotorCycleInsertDTO motorCycleInsertDTO) throws AppObjectInvalidArgumentException {
        // Έλεγχος αν υπάρχει ήδη μοτοσυκλέτα με την ίδια πινακίδα
        if (vehicleRepository.findByPlateNumber(motorCycleInsertDTO.getPlateNumber()).isPresent()) {
            throw new AppObjectInvalidArgumentException("Vehicle", "Motorcycle with plate number " + motorCycleInsertDTO.getPlateNumber() + " already exists.");
        }

        //  finding customer according to afm
        Customer customer = customerRepository.findByUserAfm(motorCycleInsertDTO.getAfm())
                .orElseThrow(() -> new AppObjectInvalidArgumentException("Customer", "Customer not found with Afm: " + motorCycleInsertDTO.getAfm()));


        Motorcycle motorcycle = mapper.mapToMotorcycleEntity(motorCycleInsertDTO, customer);

        Motorcycle savedMotorcycle = vehicleRepository.save(motorcycle);
        return mapper.mapToMotorCycleReadOnlyDTO(savedMotorcycle);
    }


}
