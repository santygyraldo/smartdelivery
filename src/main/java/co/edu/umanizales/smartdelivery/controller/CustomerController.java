package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // las respuestas de los metodos seran en formato json
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    } 

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // @responseStatus indica que el metodo retorna un codigo de estado
    public Customer create(@Valid @RequestBody Customer customer) { // valid significa que se validara que el objeto tenga todos los campos
        return customerService.create(customer);
    } // Crea un nuevo cliente

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @GetMapping("/document/{document}")
    public Customer findByDocument(@PathVariable String document) {
        return customerService.findByDocument(document);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @Valid @RequestBody Customer customer) { // actualiza un cliente
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Indica que el metodo no retorna nada
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }
}

