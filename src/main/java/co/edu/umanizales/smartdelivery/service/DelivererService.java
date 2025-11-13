package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Deliverer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DelivererService {

    private final List<Deliverer> deliverers = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

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

    private boolean existsByDocument(String document) {
        for (Deliverer d : deliverers) {
            if (d.getDocument().equals(document)) {
                return true;
            }
        }
        return false;
    }
}
