package carental.controller;

import carental.model.Vehicle;
import carental.service.VehicleService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public List<Vehicle> getAllAvailable(LocalDate startDate, LocalDate endDate) {
        return vehicleService.findAllAvailableOnCertainTime(startDate, endDate);
    }
}
