package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Motorcycle;
import co.edu.umanizales.smartdelivery.model.Truck;
import co.edu.umanizales.smartdelivery.model.Van;
import co.edu.umanizales.smartdelivery.model.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import co.edu.umanizales.smartdelivery.service.CsvService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VehicleService {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);
    private final CsvService csvService;

    public VehicleService(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostConstruct
    private void loadFromCsvAtStartup() {
        try {
            Path file = Paths.get("data").resolve("vehicles.csv");
            if (!Files.exists(file)) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {
                CsvToBean<CsvService.VehicleCsv> csvToBean = new CsvToBeanBuilder<CsvService.VehicleCsv>(reader)
                        .withType(CsvService.VehicleCsv.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();
                List<CsvService.VehicleCsv> flat = csvToBean.parse();
                List<Vehicle> loaded = new ArrayList<>();
                long maxId = 0L;
                for (CsvService.VehicleCsv fv : flat) {
                    if (fv == null) continue;
                    String type = fv.getType();
                    Vehicle v;
                    if (type != null && type.equalsIgnoreCase("motorcycle")) {
                        v = new Motorcycle(fv.getPlate());
                    } else if (type != null && type.equalsIgnoreCase("van")) {
                        v = new Van(fv.getPlate());
                    } else if (type != null && type.equalsIgnoreCase("truck")) {
                        v = new Truck(fv.getPlate());
                    } else {
                        continue;
                    }
                    v.setId(fv.getId());
                    loaded.add(v);
                    if (fv.getId() != null && fv.getId() > maxId) {
                        maxId = fv.getId();
                    }
                }
                vehicles.clear();
                vehicles.addAll(loaded);
                idSequence.set(maxId + 1);
            }
        } catch (Exception ignored) {
        }
    }

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
        csvService.exportVehicles(vehicles);
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
                csvService.exportVehicles(vehicles);
                return v;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(id)) {
                vehicles.remove(i);
                csvService.exportVehicles(vehicles);
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
