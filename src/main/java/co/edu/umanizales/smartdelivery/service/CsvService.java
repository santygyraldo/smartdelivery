package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.dto.OrderCsvDTO;
import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.model.Vehicle;
import co.edu.umanizales.smartdelivery.dto.DelivererCsvDTO;
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

    private Path ensureDataDir() { // Se encarga de crear la carpeta data si no existe
        Path dir = Paths.get("data");
        if (!Files.exists(dir)) { // Si no existe
            try {
                Files.createDirectories(dir); // Crea la carpeta data
            } catch (IOException e) { // Si hay un error
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear la carpeta data", e);
            }
        }
        return dir;
    }

    private <T> void writeBeans(List<T> beans, Class<T> type, Path file) { // Se encarga de escribir los datos en el archivo csv
        try {
            if (!Files.exists(file.getParent())) { // Si no existe la carpeta data
                Files.createDirectories(file.getParent()); // Crea la carpeta data
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file), StandardCharsets.UTF_8)); // Crea el archivo csv
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>(); // <T> significa que es un tipo genérico es decir que puede ser cualquier tipo de dato
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

    public Path exportCustomers(List<Customer> list) { // path es la ruta del archivo
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
        List<DelivererCsvDTO> flat = new ArrayList<>();
        for (Deliverer d : list) {
            DelivererCsvDTO dto = new DelivererCsvDTO();
            dto.setId(d.getId());
            dto.setName(d.getName());
            dto.setDocument(d.getDocument());
            dto.setPhone(d.getPhone());
            dto.setAvailable(d.isAvailable());
            dto.setVehicleId(d.getVehicle() != null ? d.getVehicle().getId() : null);
            flat.add(dto);
        }
        writeBeans(flat, DelivererCsvDTO.class, file);
        return file;
    }

    public Path exportOrders(List<Order> list) {
        Path file = ensureDataDir().resolve("orders.csv");
        List<OrderCsvDTO> flat = new ArrayList<>();
        for (Order o : list) {
            OrderCsvDTO dto = new OrderCsvDTO();
            dto.setId(o.getId());
            dto.setStatus(o.getStatus() != null ? o.getStatus().name() : null);
            dto.setCustomerId(o.getCustomer() != null ? o.getCustomer().getId() : null);
            dto.setPackageId(o.getOrderPackage() != null ? o.getOrderPackage().getId() : null);
            dto.setDelivererId(o.getDeliverer() != null ? o.getDeliverer().getId() : null);
            dto.setRegistrationDate(o.getRegistrationDate() != null ? o.getRegistrationDate().toString() : null);
            dto.setDeliveryDate(o.getDeliveryDate() != null ? o.getDeliveryDate().toString() : null);
            flat.add(dto);
        }
        writeBeans(flat, OrderCsvDTO.class, file);
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
