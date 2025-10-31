package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Package;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PackageService {
    private static final String CSV_FILE = "packages.csv";
    private final List<Package> packages = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (IOException e) {
            System.err.println("No se pudo cargar el archivo de paquetes: " + e.getMessage());
        }
    }

    public Package savePackage(Package package_) {
        if (package_.getId() == null) {
            // Nuevo paquete
            package_.setId(nextId.getAndIncrement());
            packages.add(package_);
        } else {
            // Actualizar paquete existente
            Package existing = findById(package_.getId());
            existing.setDescription(package_.getDescription());
            existing.setWeight(package_.getWeight());
            existing.setWidth(package_.getWidth());
            existing.setHeight(package_.getHeight());
            existing.setLength(package_.getLength());
            existing.setObservations(package_.getObservations());
        }
        
        try {
            saveToCsv();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el paquete", e);
        }
        
        return package_;
    }

    public List<Package> listAll() {
        return new ArrayList<>(packages);
    }

    public Package findById(Long id) {
        return packages.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró el paquete con ID: " + id));
    }

    public void deletePackage(Long id) {
        if (packages.removeIf(p -> p.getId().equals(id))) {
            try {
                saveToCsv();
            } catch (IOException e) {
                throw new RuntimeException("Error al eliminar el paquete", e);
            }
        } else {
            throw new NoSuchElementException("No se encontró el paquete con ID: " + id);
        }
    }
    
    private void saveToCsv() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Escribir encabezado
            writer.println("id,description,weight,width,height,length,observations");
            
            // Escribir datos
            for (Package pkg : packages) {
                writer.println(String.format("%d,\"%s\",%s,%s,%s,%s,\"%s\"",
                    pkg.getId(),
                    pkg.getDescription().replace("\"", "\"\""),
                    pkg.getWeight(),
                    pkg.getWidth(),
                    pkg.getHeight(),
                    pkg.getLength(),
                    pkg.getObservations() != null ? pkg.getObservations().replace("\"", "\"\"") : ""
                ));
            }
        }
    }
    
    private void loadFromCsv() throws IOException {
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return; // No hay archivo que cargar
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            // Saltar encabezado
            br.readLine();
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // Limpiar comillas de los campos de texto
                for (int i = 0; i < values.length; i++) {
                    if (values[i].startsWith("\"") && values[i].endsWith("\"")) {
                        values[i] = values[i].substring(1, values[i].length() - 1).replace("\"\"", "\"");
                    }
                }
                
                Package pkg = new Package(
                    Long.parseLong(values[0]), // id
                    values[1], // description
                    new BigDecimal(values[2]), // weight
                    new BigDecimal(values[3]), // width
                    new BigDecimal(values[4]), // height
                    new BigDecimal(values[5]), // length
                    values.length > 6 ? values[6] : null // observations
                );
                
                packages.add(pkg);
                
                // Actualizar el contador de IDs
                if (pkg.getId() >= nextId.get()) {
                    nextId.set(pkg.getId() + 1);
                }
            }
        }
    }


}
