package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.exception.CustomerException;
import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.service.CustomerService;
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
 * Proporciona endpoints para realizar operaciones CRUD en la entidad Customer.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return Lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<Customer>> listCustomers() {
        List<Customer> customers = customerService.listCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente a buscar
     * @return El cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.findById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Crea un nuevo cliente.
     *
     * @param customer Datos del cliente a crear
     * @return El cliente creado
     */
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer newCustomer = customerService.registerCustomer(customer);
            return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
        } catch (CustomerException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id ID del cliente a actualizar
     * @param customer Datos actualizados del cliente
     * @return El cliente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id, 
            @Valid @RequestBody Customer customer) {
        try {
            // Aseguramos que el ID del path coincida con el ID del cuerpo
            if (!id.equals(customer.getId())) {
                return createErrorResponse(HttpStatus.BAD_REQUEST, "The customer ID does not match the URL ID");
            }
            
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (CustomerException e) {
            return createErrorResponse(
                e.getMessage().contains("not found") ? 
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
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (CustomerException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Busca un cliente por su número de documento.
     *
     * @param document Número de documento del cliente a buscar
     * @return El cliente encontrado
     */
    @GetMapping("/search-by-document")
    public ResponseEntity<?> findCustomerByDocument(@RequestParam String document) {
        try {
            Customer customer = customerService.findByDocument(document);
            return ResponseEntity.ok(customer);
        } catch (CustomerException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Método auxiliar para crear respuestas de error estandarizadas.
     *
     * @param status Código de estado HTTP
     * @param message Mensaje de error
     * @return ResponseEntity con el mensaje de error
     */
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}
