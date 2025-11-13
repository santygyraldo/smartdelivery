package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Deliverer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DelivererService {

    private final List<Deliverer> deliverers = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    // Asignaciones en memoria: vehicleId -> delivererId (llave primaria: ID del vehículo)
    private final Map<Long, Long> vehicleAssignments = new HashMap<>();

    private final VehicleService vehicleService;

    public DelivererService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public Deliverer create(Deliverer deliverer) {
        if (deliverer.getId() == null) {
            deliverer.setId(idSequence.getAndIncrement());
        }
        if (existsByDocument(deliverer.getDocument())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya existe");
        }
        deliverers.add(deliverer);
        return deliverer;
    }

    public List<Deliverer> findAll() {
        return deliverers;
    }

    public Deliverer findById(Long id) {
        for (Deliverer d : deliverers) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Repartidor no encontrado");
    }

    public Deliverer update(Long id, Deliverer update) {
        for (Deliverer d : deliverers) {
            if (d.getId().equals(id)) {
                if (update.getName() != null) d.setName(update.getName());
                if (update.getDocument() != null && !update.getDocument().equals(d.getDocument()) && existsByDocument(update.getDocument())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya existe");
                }
                if (update.getDocument() != null) d.setDocument(update.getDocument());
                if (update.getPhone() != null) d.setPhone(update.getPhone());
                return d;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Repartidor no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < deliverers.size(); i++) {
            if (deliverers.get(i).getId().equals(id)) {
                // Si el repartidor tiene un vehículo asignado, desasignarlo
                Long vehicleIdToRemove = null;
                for (Map.Entry<Long, Long> entry : vehicleAssignments.entrySet()) {
                    if (entry.getValue().equals(id)) {
                        vehicleIdToRemove = entry.getKey();
                        break;
                    }
                }
                if (vehicleIdToRemove != null) {
                    vehicleAssignments.remove(vehicleIdToRemove);
                }
                deliverers.remove(i);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Repartidor no encontrado");
    }

    public Deliverer setAvailability(Long id, boolean available) {
        Deliverer d = findById(id);
        d.setAvailable(available);
        return d;
    }

    public Deliverer findAvailable() {
        for (Deliverer d : deliverers) {
            if (d.isAvailable()) {
                return d;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay repartidores disponibles");
    }

    // ===== Gestión de asignaciones vehículo -> repartidor =====

    public void assignVehicle(Long vehicleId, Long delivererId) {
        // Validar existencia
        vehicleService.findById(vehicleId);
        findById(delivererId);

        // Verificar si el vehículo ya está asignado
        if (vehicleAssignments.containsKey(vehicleId)) {
            Long assignedDelivererId = vehicleAssignments.get(vehicleId);
            if (!assignedDelivererId.equals(delivererId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vehículo ya está asignado a otro repartidor");
            }
            // Si ya está asignado al mismo repartidor, no hacer nada
            return;
        }

        // Verificar si el repartidor ya tiene un vehículo asignado
        for (Map.Entry<Long, Long> entry : vehicleAssignments.entrySet()) {
            if (entry.getValue().equals(delivererId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El repartidor ya tiene un vehículo asignado");
            }
        }

        vehicleAssignments.put(vehicleId, delivererId);
    }

    public void unassignByVehicle(Long vehicleId) {
        // Validar existencia de vehículo
        vehicleService.findById(vehicleId);
        if (!vehicleAssignments.containsKey(vehicleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vehículo no está asignado");
        }
        vehicleAssignments.remove(vehicleId);
    }

    public Deliverer getDelivererByVehicle(Long vehicleId) {
        // Validar existencia de vehículo
        vehicleService.findById(vehicleId);
        Long delivererId = vehicleAssignments.get(vehicleId);
        if (delivererId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vehículo no está asignado a ningún repartidor");
        }
        return findById(delivererId);
    }

    public Long getVehicleByDeliverer(Long delivererId) {
        findById(delivererId);
        for (Map.Entry<Long, Long> entry : vehicleAssignments.entrySet()) {
            if (entry.getValue().equals(delivererId)) {
                return entry.getKey();
            }
        }
        return null; // sin asignación
    }

    private boolean existsByDocument(String document) {
        for (Deliverer d : deliverers) {
            if (d.getDocument().equals(document)) {
                return true;
            }
        }
        return false;
    }
}
