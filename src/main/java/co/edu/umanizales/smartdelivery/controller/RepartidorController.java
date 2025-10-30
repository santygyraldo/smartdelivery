package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Repartidor;
import co.edu.umanizales.smartdelivery.service.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {

    private final RepartidorService repartidorService;

    @Autowired
    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    @GetMapping
    public ResponseEntity<List<Repartidor>> listarRepartidores() {
        List<Repartidor> repartidores = repartidorService.listarRepartidores();
        return ResponseEntity.ok(repartidores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRepartidorPorId(@PathVariable Long id) {
        try {
            Repartidor repartidor = repartidorService.buscarPorId(id);
            return ResponseEntity.ok(repartidor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearRepartidor(@RequestBody Repartidor repartidor) {
        try {
            Repartidor nuevoRepartidor = repartidorService.registrarRepartidor(repartidor);
            return new ResponseEntity<>(nuevoRepartidor, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRepartidor(
            @PathVariable Long id,
            @RequestBody Repartidor repartidor) {
        try {
            if (!id.equals(repartidor.getId())) {
                return ResponseEntity.badRequest()
                        .body("El ID del repartidor no coincide con el ID de la URL");
            }
            Repartidor repartidorActualizado = repartidorService.actualizarRepartidor(id, repartidor);
            return ResponseEntity.ok(repartidorActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRepartidor(@PathVariable Long id) {
        try {
            repartidorService.eliminarRepartidor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Repartidor>> listarRepartidoresDisponibles() {
        List<Repartidor> repartidores = repartidorService.listarRepartidoresDisponibles(true);
        return ResponseEntity.ok(repartidores);
    }

    @GetMapping("/no-disponibles")
    public ResponseEntity<List<Repartidor>> listarRepartidoresNoDisponibles() {
        List<Repartidor> repartidores = repartidorService.listarRepartidoresDisponibles(false);
        return ResponseEntity.ok(repartidores);
    }
}
