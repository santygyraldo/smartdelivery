package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.dto.CreateOrderRequest;
import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.OrderStatus;
import co.edu.umanizales.smartdelivery.service.CustomerService;
import co.edu.umanizales.smartdelivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        // Buscar el cliente por ID
        Customer customer = customerService.findById(request.getCustomerId());
        if (customer == null) {
            throw new NoSuchElementException("No se encontró el cliente con ID: " + request.getCustomerId());
        }
        
        // Crear la orden a partir del DTO
        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPackage_(request.getPackage_());
        
        // Establecer el estado, usando PENDING como valor por defecto si no se especifica
        String status = request.getStatus() != null ? request.getStatus() : "PENDING";
        order.setStatus(OrderStatus.valueOf(status));
        
        Order newOrder = orderService.createOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> listOrders() {
        return ResponseEntity.ok(orderService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Not found",
                        "message", e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam OrderStatus status) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Not found",
                        "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Bad request",
                        "message", e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}/assign-deliverer/{delivererId}")
    public ResponseEntity<?> assignDeliverer(
            @PathVariable Long id, 
            @PathVariable Long delivererId) {
        try {
            Order order = orderService.assignDeliverer(id, delivererId);
            return ResponseEntity.ok(order);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Not found",
                        "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Bad request",
                        "message", e.getMessage()
                    ));
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> findByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.findByCustomer(customerId));
    }

    @GetMapping("/deliverer/{delivererId}")
    public ResponseEntity<List<Order>> findByDeliverer(@PathVariable Long delivererId) {
        return ResponseEntity.ok(orderService.findByDeliverer(delivererId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> findByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.findByStatus(status));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Not found",
                        "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Bad request",
                        "message", e.getMessage()
                    ));
        }
    }
}
