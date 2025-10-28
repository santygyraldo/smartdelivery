package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.EstadoPedido;
import co.edu.umanizales.smartdelivery.model.Pedido;
import co.edu.umanizales.smartdelivery.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoService.buscarPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "No encontrado",
                        "message", e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Long id, 
            @RequestParam EstadoPedido estado) {
        try {
            return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, estado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "No encontrado",
                        "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Solicitud incorrecta",
                        "message", e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}/asignar-repartidor/{repartidorId}")
    public ResponseEntity<?> asignarRepartidor(
            @PathVariable Long id, 
            @PathVariable Long repartidorId) {
        try {
            Pedido pedido = pedidoService.asignarRepartidor(id, repartidorId);
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "No encontrado",
                        "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Solicitud incorrecta",
                        "message", e.getMessage()
                    ));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.buscarPorCliente(clienteId));
    }

    @GetMapping("/repartidor/{repartidorId}")
    public ResponseEntity<List<Pedido>> buscarPorRepartidor(@PathVariable Long repartidorId) {
        return ResponseEntity.ok(pedidoService.buscarPorRepartidor(repartidorId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(@PathVariable EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.buscarPorEstado(estado));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "No encontrado",
                        "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Solicitud incorrecta",
                        "message", e.getMessage()
                    ));
        }
    }
}
