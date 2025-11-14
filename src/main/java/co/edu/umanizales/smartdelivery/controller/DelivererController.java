package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.service.DelivererService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/deliverers")
public class DelivererController {

    static class DelivererResponse {
        public Long id;
        public String name;
        public String document;
        public String phone;
        public boolean available;
        public Long vehicleId;
    }

    private DelivererResponse toResponse(Deliverer d) { // DelivererResponse es una clase que se encarga de convertir un objeto Deliverer en un objeto JSON
        DelivererResponse r = new DelivererResponse();
        r.id = d.getId();
        r.name = d.getName();
        r.document = d.getDocument();
        r.phone = d.getPhone();
        r.available = d.isAvailable();
        r.vehicleId = delivererService.getVehicleByDeliverer(d.getId());
        return r;
    }

    private final DelivererService delivererService;

    public DelivererController(DelivererService delivererService) {
        this.delivererService = delivererService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DelivererResponse create(@Valid @RequestBody Deliverer deliverer) {
        Deliverer created = delivererService.create(deliverer);
        return toResponse(created);
    }

    @GetMapping
    public List<DelivererResponse> findAll() {
        List<Deliverer> list = delivererService.findAll();
        List<DelivererResponse> out = new ArrayList<>();
        for (Deliverer d : list) {
            out.add(toResponse(d));
        }
        return out;
    }

    @GetMapping("/{id}")
    public DelivererResponse findById(@PathVariable Long id) { //@pathVariable es un anotacion que indica que el valor de la variable id viene de la URL
        Deliverer d = delivererService.findById(id);
        return toResponse(d);
    }

    @PutMapping("/{id}")
    public DelivererResponse update(@PathVariable Long id, @Valid @RequestBody Deliverer deliverer) { //@RequestBody es un anotacion que indica que el valor de la variable deliverer viene del cuerpo de la peticion
        Deliverer upd = delivererService.update(id, deliverer);
        return toResponse(upd);
    }

    @DeleteMapping("/{id}") // Elimina un entregador por su ID
    @ResponseStatus(HttpStatus.NO_CONTENT) //@ResponseStatus es un anotacion que indica que el valor de la variable id viene de la URL
    public void delete(@PathVariable Long id) {
        delivererService.delete(id);
    }

    @PutMapping("/{id}/availability") // Actualiza la disponibilidad de un entregador
    public Deliverer setAvailability(@PathVariable Long id, @RequestParam boolean available) { //@RequestParam es un anotacion que indica que el valor de la variable available viene de la URL
        return delivererService.setAvailability(id, available);
    }

    @GetMapping("/available/one")
    public DelivererResponse findAvailable() { //findAvailable es un metodo que busca un entregador disponible
        Deliverer d = delivererService.findAvailable();
        return toResponse(d);
    }

    @PutMapping("/{delivererId}/vehicle/{vehicleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignVehicle(@PathVariable Long delivererId, @PathVariable Long vehicleId) {
        delivererService.assignVehicle(vehicleId, delivererId);
    }

    @DeleteMapping("/vehicle/{vehicleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignByVehicle(@PathVariable Long vehicleId) {
        delivererService.unassignByVehicle(vehicleId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public Deliverer getDelivererByVehicle(@PathVariable Long vehicleId) {
        return delivererService.getDelivererByVehicle(vehicleId);
    }

    @GetMapping("/{delivererId}/vehicle")
    public Long getVehicleByDeliverer(@PathVariable Long delivererId) {
        return delivererService.getVehicleByDeliverer(delivererId);
    }
}
