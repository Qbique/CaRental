package carental.service;

import carental.model.Rental;
import carental.repository.RentalRepository;
import org.springframework.stereotype.Service;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void saveRental(Rental rental) {
        rentalRepository.save(rental);
    }
}
