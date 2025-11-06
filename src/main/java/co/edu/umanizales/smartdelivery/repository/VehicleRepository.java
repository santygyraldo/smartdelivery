package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VehicleRepository {
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            vehicle.setId(nextId.getAndIncrement());
            vehicles.add(vehicle);
        } else {
            Vehicle existing = findById(vehicle.getId());
            existing.setPlate(vehicle.getPlate());
        }
        return vehicle;
    }

    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    public Vehicle findById(Long id) {
        return vehicles.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró el vehículo con ID: " + id));
    }

    public void delete(Long id) {
        if (!vehicles.removeIf(v -> v.getId().equals(id))) {
            throw new NoSuchElementException("No se encontró el vehículo con ID: " + id);
        }
    }

    public Vehicle findByPlate(String plate) {
        return vehicles.stream()
                .filter(v -> v.getPlate().equals(plate))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró vehículo con placa: " + plate));
    }
}
