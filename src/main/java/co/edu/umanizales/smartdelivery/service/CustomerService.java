package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public Customer create(Customer customer) { // Crea un cliente
        if (customer.getId() == null) {
            customer.setId(idSequence.getAndIncrement());
        }
        if (existsByDocument(customer.getDocument())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya existe");
        }
        customers.add(customer);
        return customer;
    }

    public List<Customer> findAll() {
        return customers;
    }

    public Customer findById(Long id) { // Busca un cliente por su ID
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    public Customer update(Long id, Customer update) { // Actualiza un cliente por su ID
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                if (update.getDocument() != null && !update.getDocument().equals(c.getDocument()) && existsByDocument(update.getDocument())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya existe");
                }
                if (update.getDocument() != null) c.setDocument(update.getDocument());
                if (update.getName() != null) c.setName(update.getName());
                if (update.getPhone() != null) c.setPhone(update.getPhone());
                return c;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    public void delete(Long id) { // Elimina un cliente por su ID
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(id)) {
                customers.remove(i);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    public Customer findByDocument(String document) { // Busca un cliente por su documento
        for (Customer c : customers) {
            if (c.getDocument().equals(document)) {
                return c;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    private boolean existsByDocument(String document) { // Verifica si un cliente existe por su documento
        for (Customer c : customers) {
            if (c.getDocument().equals(document)) {
                return true;
            }
        }
        return false;
    }
}
