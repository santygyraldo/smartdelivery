package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Repartidor;
import co.edu.umanizales.smartdelivery.service.RepartidorService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Repartidor> registrarRepartidor(@Valid @RequestBody Repartidor repartidor) {
        Repartidor nuevoRepartidor = repartidorService.registrarRepartidor(repartidor);
        return new ResponseEntity<>(nuevoRepartidor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Repartidor>> listarRepartidores() {
        return ResponseEntity.ok(repartidorService.listarRepartidores());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Repartidor>> listarRepartidoresDisponibles() {
        return ResponseEntity.ok(repartidorService.listarRepartidoresDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repartidor> obtenerRepartidor(@PathVariable Long id) {
        return ResponseEntity.ok(repartidorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repartidor> actualizarRepartidor(
            @PathVariable Long id, 
            @Valid @RequestBody Repartidor repartidor) {
        return ResponseEntity.ok(repartidorService.actualizarRepartidor(id, repartidor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRepartidor(@PathVariable Long id) {
        repartidorService.eliminarRepartidor(id);
        return ResponseEntity.noContent().build();
    }
}
