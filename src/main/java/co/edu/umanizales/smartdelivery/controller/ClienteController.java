package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.exception.ClienteException;
import co.edu.umanizales.smartdelivery.model.Cliente;
import co.edu.umanizales.smartdelivery.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones relacionadas con clientes.
 * Proporciona endpoints para realizar operaciones CRUD en la entidad Cliente.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return Lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente a buscar
     * @return El cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (ClienteException e) {
            return crearErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Crea un nuevo cliente.
     *
     * @param cliente Datos del cliente a crear
     * @return El cliente creado
     */
    @PostMapping
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.registrarCliente(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (ClienteException e) {
            return crearErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id ID del cliente a actualizar
     * @param cliente Datos actualizados del cliente
     * @return El cliente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id, 
            @Valid @RequestBody Cliente cliente) {
        try {
            // Aseguramos que el ID del path coincida con el ID del cuerpo
            if (!id.equals(cliente.getId())) {
                return crearErrorResponse(HttpStatus.BAD_REQUEST, "El ID del cliente no coincide con el ID de la URL");
            }
            
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (ClienteException e) {
            return crearErrorResponse(
                e.getMessage().contains("no encontrado") ? 
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST, 
                e.getMessage()
            );
        }
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id ID del cliente a eliminar
     * @return Respuesta vacía con código 204 (No Content) si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (ClienteException e) {
            return crearErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param email Correo electrónico del cliente a buscar
     * @return El cliente encontrado
     */
    @GetMapping("/buscar-por-email")
    public ResponseEntity<?> buscarClientePorEmail(@RequestParam String email) {
        try {
            Cliente cliente = clienteService.buscarPorEmail(email);
            return ResponseEntity.ok(cliente);
        } catch (ClienteException e) {
            return crearErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Método auxiliar para crear respuestas de error estandarizadas.
     *
     * @param status Código de estado HTTP
     * @param message Mensaje de error
     * @return ResponseEntity con el mensaje de error
     */
    private ResponseEntity<Map<String, Object>> crearErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}
