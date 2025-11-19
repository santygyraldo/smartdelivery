package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // las respuestas de los metodos seran en formato json
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody Order order) {
        return orderService.create(order);
    }

    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public Order findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody Order order) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }

    @PutMapping("/{id}/assign/{delivererId}")
    public Order assignDeliverer(@PathVariable Long id, @PathVariable Long delivererId) {
        return orderService.assignDeliverer(id, delivererId);
    }

    @PutMapping("/{id}/unassign")
    public Order unassignDeliverer(@PathVariable Long id) {
        return orderService.unassignDeliverer(id);
    }
}
