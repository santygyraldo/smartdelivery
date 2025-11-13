package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.service.DelivererService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliverers")
public class DelivererController {

    private final DelivererService delivererService;

    public DelivererController(DelivererService delivererService) {
        this.delivererService = delivererService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Deliverer create(@Valid @RequestBody Deliverer deliverer) {
        return delivererService.create(deliverer);
    }

    @GetMapping
    public List<Deliverer> findAll() {
        return delivererService.findAll();
    }

    @GetMapping("/{id}")
    public Deliverer findById(@PathVariable Long id) {
        return delivererService.findById(id);
    }

    @PutMapping("/{id}")
    public Deliverer update(@PathVariable Long id, @Valid @RequestBody Deliverer deliverer) {
        return delivererService.update(id, deliverer);
    }

    @DeleteMapping("/{id}") // Elimina un entregador por su ID
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        delivererService.delete(id);
    }

    @PutMapping("/{id}/availability") // Actualiza la disponibilidad de un entregador
    public Deliverer setAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return delivererService.setAvailability(id, available);
    }

    @GetMapping("/available/one")
    public Deliverer findAvailable() {
        return delivererService.findAvailable();
    }
}
