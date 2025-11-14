package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.model.Vehicle;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private Path ensureDataDir() {
        Path dir = Paths.get("data");
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear la carpeta data", e);
            }
        }
        return dir;
    }

    private <T> void writeBeans(List<T> beans, Class<T> type, Path file) {
        try {
            if (!Files.exists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file), StandardCharsets.UTF_8));
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(type);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            beanToCsv.write(beans);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error exportando CSV: " + file.getFileName(), e);
        }
    }

    public Path exportCustomers(List<Customer> list) {
        Path file = ensureDataDir().resolve("customers.csv");
        writeBeans(list, Customer.class, file);
        return file;
    }

    public Path exportPackages(List<Package> list) {
        Path file = ensureDataDir().resolve("packages.csv");
        writeBeans(list, Package.class, file);
        return file;
    }

    public Path exportDeliverers(List<Deliverer> list) {
        Path file = ensureDataDir().resolve("deliverers.csv");
        writeBeans(list, Deliverer.class, file);
        return file;
    }

    public Path exportOrders(List<Order> list) {
        Path file = ensureDataDir().resolve("orders.csv");
        writeBeans(list, Order.class, file);
        return file;
    }

    // Para vehículos, generamos un bean plano con id, plate y type
    public static class VehicleCsv {
        @com.opencsv.bean.CsvBindByName(column = "ID")
        private Long id;
        @com.opencsv.bean.CsvBindByName(column = "PLATE")
        private String plate;
        @com.opencsv.bean.CsvBindByName(column = "TYPE")
        private String type;
        public VehicleCsv() {}
        public VehicleCsv(Long id, String plate, String type) {
            this.id = id; this.plate = plate; this.type = type;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPlate() { return plate; }
        public void setPlate(String plate) { this.plate = plate; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public Path exportVehicles(List<Vehicle> list) {
        Path file = ensureDataDir().resolve("vehicles.csv");
        List<VehicleCsv> flat = new ArrayList<>();
        for (Vehicle v : list) {
            String type = null;
            if (v != null) {
                type = v.getVehicleType();
            }
            flat.add(new VehicleCsv(v.getId(), v.getPlate(), type));
        }
        writeBeans(flat, VehicleCsv.class, file);
        return file;
    }
}
