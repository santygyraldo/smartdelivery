package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Package;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
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

import co.edu.umanizales.smartdelivery.service.CsvService;

@Service
public class PackageService {

    private final List<Package> packages = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);
    private final CsvService csvService;

    public PackageService(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostConstruct
    private void loadFromCsvAtStartup() {
        try {
            Path file = Paths.get("data").resolve("packages.csv");
            if (!Files.exists(file)) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {
                HeaderColumnNameMappingStrategy<Package> strategy = new HeaderColumnNameMappingStrategy<>();
                strategy.setType(Package.class);
                CsvToBean<Package> csvToBean = new CsvToBeanBuilder<Package>(reader)
                        .withMappingStrategy(strategy)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();
                List<Package> loaded = csvToBean.parse();
                packages.clear();
                packages.addAll(loaded);
                long maxId = loaded.stream()
                        .filter(p -> p.getId() != null)
                        .mapToLong(Package::getId)
                        .max()
                        .orElse(0L);
                idSequence.set(maxId + 1);
            }
        } catch (Exception ignored) {
        }
    }

    public Package create(Package pkg) {
        if (pkg.getRegistrationDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de registro del paquete es obligatoria");
        }
        if (pkg.getId() == null) {
            pkg.setId(idSequence.getAndIncrement());
        }
        packages.add(pkg);
        csvService.exportPackages(packages);
        return pkg;
    }

    public List<Package> findAll() {
        return packages;
    }

    public Package findById(Long id) {
        for (Package p : packages) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
    }

    public Package update(Long id, Package update) {
        for (Package p : packages) {
            if (p.getId().equals(id)) {
                if (update.getWeight() != null) p.setWeight(update.getWeight());
                if (update.getDescription() != null) p.setDescription(update.getDescription());
                if (update.getRecipientName() != null) p.setRecipientName(update.getRecipientName());
                if (update.getDeliveryAddress() != null) p.setDeliveryAddress(update.getDeliveryAddress());
                csvService.exportPackages(packages);
                return p;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < packages.size(); i++) {
            if (packages.get(i).getId().equals(id)) {
                packages.remove(i);
                csvService.exportPackages(packages);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
    }
}
