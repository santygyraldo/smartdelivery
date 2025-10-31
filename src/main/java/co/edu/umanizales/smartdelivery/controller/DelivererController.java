package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.service.DelivererService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliverers")
public class DelivererController {

    private final DelivererService delivererService;

    @Autowired
    public DelivererController(DelivererService delivererService) {
        this.delivererService = delivererService;
    }

    @GetMapping
    public ResponseEntity<List<Deliverer>> listDeliverers() {
        List<Deliverer> deliverers = delivererService.listDeliverers();
        return ResponseEntity.ok(deliverers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDelivererById(@PathVariable Long id) {
        try {
            Deliverer deliverer = delivererService.findById(id);
            return ResponseEntity.ok(deliverer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createDeliverer(@RequestBody Deliverer deliverer) {
        try {
            Deliverer newDeliverer = delivererService.registerDeliverer(deliverer);
            return new ResponseEntity<>(newDeliverer, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeliverer(
            @PathVariable Long id,
            @RequestBody Deliverer deliverer) {
        try {
            if (!id.equals(deliverer.getId())) {
                return ResponseEntity.badRequest()
                        .body("The deliverer ID does not match the URL ID");
            }
            Deliverer updatedDeliverer = delivererService.updateDeliverer(id, deliverer);
            return ResponseEntity.ok(updatedDeliverer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliverer(@PathVariable Long id) {
        try {
            delivererService.deleteDeliverer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Deliverer>> listAvailableDeliverers() {
        List<Deliverer> deliverers = delivererService.listAvailableDeliverers(true);
        return ResponseEntity.ok(deliverers);
    }

    @GetMapping("/unavailable")
    public ResponseEntity<List<Deliverer>> listUnavailableDeliverers() {
        List<Deliverer> deliverers = delivererService.listAvailableDeliverers(false);
        return ResponseEntity.ok(deliverers);
    }
}
