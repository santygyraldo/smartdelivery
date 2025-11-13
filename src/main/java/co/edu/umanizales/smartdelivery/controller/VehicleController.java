package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Vehicle;
import co.edu.umanizales.smartdelivery.service.VehicleService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public static record PlateRequest(@NotBlank String plate) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle create(@RequestParam String type, @RequestBody PlateRequest request) {
        return vehicleService.create(type, request.plate());
    }

    @GetMapping
    public List<Vehicle> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @GetMapping("/plate/{plate}")
    public Vehicle findByPlate(@PathVariable String plate) {
        return vehicleService.findByPlate(plate);
    }

    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody PlateRequest request) {
        return vehicleService.update(id, request.plate());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehicleService.delete(id);
    }
}
