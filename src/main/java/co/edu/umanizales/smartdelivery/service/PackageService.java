package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Package;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PackageService {

    private final List<Package> packages = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public Package create(Package pkg) {
        if (pkg.getId() == null) {
            pkg.setId(idSequence.getAndIncrement());
        }
        packages.add(pkg);
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
                return p;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < packages.size(); i++) {
            if (packages.get(i).getId().equals(id)) {
                packages.remove(i);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
    }
}
