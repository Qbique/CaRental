package carental.service;

import carental.model.Rental;
import carental.model.Vehicle;
import carental.repository.RentalRepository;
import carental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Service
public class VehicleService {

    @Value("${query.all.available.on.dates}")
    private String allAvailableOnDates;

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final EntityManager entityManager;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository, EntityManager entityManager) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.entityManager = entityManager;
    }

    private List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAllVehiclesOnStore() {
        List<Vehicle> allVehicles = this.findAll();
        List<Rental> rentalVehicles = rentalRepository.findAll();
        LocalDate now = LocalDate.now();

        for (Rental rental : rentalVehicles) {
            if (now.isBefore(rental.getEndDate()) && now.isAfter(rental.getStartDate())) {
                allVehicles.remove(rental.getVehicle());
            }
        }
        return allVehicles;
    }

    public List<Vehicle> findAllAvailableOnCertainTime(LocalDate startDate, LocalDate endDate) {

        Query allAvailableOnDatesQuery =
                entityManager.createNativeQuery(allAvailableOnDates, Vehicle.class)
                        .setParameter(1, startDate)
                        .setParameter(2, endDate);

        List<Vehicle> allAvailable = allAvailableOnDatesQuery.getResultList();

        return allAvailable;
    }
}

