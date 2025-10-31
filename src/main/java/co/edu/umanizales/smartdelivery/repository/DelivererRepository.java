package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.service.CsvService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DelivererRepository extends CsvService<Deliverer> {

    @Override
    protected String getCsvFileName() {
        return "deliverers.csv";
    }

    @Override
    protected Class<Deliverer> getEntityClass() {
        return Deliverer.class;
    }

    public Optional<Deliverer> findById(Long id) {
        return findAll().stream()
                .filter(r -> r.getId() != null && r.getId().equals(id))
                .findFirst();
    }

    public Optional<Deliverer> findByDocument(String document) {
        return findAll().stream()
                .filter(r -> r.getDocument() != null && r.getDocument().equals(document))
                .findFirst();
    }

    public Optional<Deliverer> findByVehiclePlate(String plate) {
        return findAll().stream()
                .filter(r -> r.getVehiclePlate() != null && r.getVehiclePlate().equals(plate))
                .findFirst();
    }

    public void update(Deliverer deliverer) {
        var items = findAll();
        items.removeIf(r -> r.getId().equals(deliverer.getId()));
        items.add(deliverer);
        saveAll(items);
    }

    public void deleteById(Long id) {
        var items = findAll();
        items.removeIf(r -> r.getId().equals(id));
        saveAll(items);
    }

    public boolean existsByDocument(String document) {
        return findAll().stream()
                .anyMatch(r -> r.getDocument() != null && r.getDocument().equals(document));
    }

    public boolean existsByVehiclePlate(String vehiclePlate) {
        return findAll().stream()
                .anyMatch(r -> r.getVehiclePlate() != null && r.getVehiclePlate().equals(vehiclePlate));
    }

    public boolean existsByDocumentAndIdNot(String document, Long id) {
        return findAll().stream()
                .anyMatch(r -> r.getDocument() != null && r.getDocument().equals(document) && !r.getId().equals(id));
    }

    public boolean existsByVehiclePlateAndIdNot(String vehiclePlate, Long id) {
        return findAll().stream()
                .anyMatch(r -> r.getVehiclePlate() != null && r.getVehiclePlate().equals(vehiclePlate) && !r.getId().equals(id));
    }

    public List<Deliverer> findByAvailable(boolean available) {
        return findAll().stream()
                .filter(r -> r.isAvailable() == available)
                .toList();
    }
}
