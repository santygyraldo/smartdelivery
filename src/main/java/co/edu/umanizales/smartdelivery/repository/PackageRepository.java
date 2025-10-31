package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.service.CsvService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PackageRepository extends CsvService<Package> {

    @Override
    protected String getCsvFileName() {
        return "packages.csv";
    }

    @Override
    protected Class<Package> getEntityClass() {
        return Package.class;
    }

    public Optional<Package> findById(Long id) {
        return findAll().stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst();
    }

    public void update(Package package_) {
        var items = findAll();
        items.removeIf(p -> p.getId().equals(package_.getId()));
        items.add(package_);
        saveAll(items);
    }

    public void deleteById(Long id) {
        var items = findAll();
        items.removeIf(p -> p.getId().equals(id));
        saveAll(items);
    }
}
