package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Paquete;
import co.edu.umanizales.smartdelivery.service.PaqueteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paquetes")
public class PaqueteController {

    private final PaqueteService paqueteService;

    @Autowired
    public PaqueteController(PaqueteService paqueteService) {
        this.paqueteService = paqueteService;
    }

    @PostMapping
    public ResponseEntity<Paquete> crearPaquete(@Valid @RequestBody Paquete paquete) {
        Paquete nuevoPaquete = paqueteService.guardarPaquete(paquete);
        return new ResponseEntity<>(nuevoPaquete, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Paquete>> listarPaquetes() {
        return ResponseEntity.ok(paqueteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> obtenerPaquete(@PathVariable Long id) {
        return ResponseEntity.ok(paqueteService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaquete(@PathVariable Long id) {
        paqueteService.eliminarPaquete(id);
        return ResponseEntity.noContent().build();
    }
}
