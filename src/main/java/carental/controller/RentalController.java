package carental.controller;

import carental.model.Rental;
import carental.service.RentalService;
import org.springframework.stereotype.Controller;

@Controller
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public void addRental(Rental rental) {
        rentalService.saveRental(rental);
    }
}

