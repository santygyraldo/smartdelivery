package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Vehicle;
import co.edu.umanizales.smartdelivery.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        if (vehicle.getPlate() == null || vehicle.getPlate().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo es obligatoria");
        }
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> listAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return vehicleRepository.findById(id);
    }

    public void deleteVehicle(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        vehicleRepository.delete(id);
    }

    public Vehicle findByPlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        return vehicleRepository.findByPlate(plate);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleData) {
        Vehicle vehicle = findById(id);
        if (vehicleData.getPlate() != null && !vehicleData.getPlate().trim().isEmpty()) {
            vehicle.setPlate(vehicleData.getPlate());
        }
        return vehicleRepository.save(vehicle);
    }
}
