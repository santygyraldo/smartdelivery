package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Motorcycle;
import co.edu.umanizales.smartdelivery.model.Truck;
import co.edu.umanizales.smartdelivery.model.Van;
import co.edu.umanizales.smartdelivery.model.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VehicleService {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public Vehicle create(String type, String plate) {
        if (existsByPlate(plate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La placa ya existe");
        }
        Vehicle v;
        if ("motorcycle".equalsIgnoreCase(type)) {
            v = new Motorcycle(plate);
        } else if ("van".equalsIgnoreCase(type)) {
            v = new Van(plate);
        } else if ("truck".equalsIgnoreCase(type)) {
            v = new Truck(plate);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de vehículo inválido");
        }
        v.setId(idSequence.getAndIncrement());
        vehicles.add(v);
        return v;
    }

    public List<Vehicle> findAll() {
        return vehicles;
    }

    public Vehicle findById(Long id) {
        for (Vehicle v : vehicles) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado");
    }

    public Vehicle findByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getPlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado");
    }

    public Vehicle update(Long id, String newPlate) {
        if (newPlate != null && existsByPlate(newPlate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La placa ya existe");
        }
        for (Vehicle v : vehicles) {
            if (v.getId().equals(id)) {
                if (newPlate != null) v.setPlate(newPlate);
                return v;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(id)) {
                vehicles.remove(i);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado");
    }

    private boolean existsByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getPlate().equalsIgnoreCase(plate)) {
                return true;
            }
        }
        return false;
    }
}
