package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.exception.CustomerException;
import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("No se encontró un cliente con el ID: " + id));
    }

    public Customer registerCustomer(Customer customer) {
        if (customerRepository.findByDocument(customer.getDocument()).isPresent()) {
            throw new CustomerException("Ya existe un cliente con este número de documento");
        }

        // Generar un nuevo ID
        Long newId = generateNewId();
        customer.setId(newId);

        customerRepository.save(customer);
        return customer;
    }

    /**
     * Genera un nuevo ID para un cliente
     * @return El siguiente ID disponible
     */
    private Long generateNewId() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            return 1L;
        }
        return customers.stream()
                .mapToLong(Customer::getId)
                .max()
                .orElse(0L) + 1L;
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = findById(id);

        // Verificar si ya existe otro cliente con el mismo documento
        customerRepository.findByDocument(customer.getDocument())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new CustomerException("El documento ya está en uso por otro cliente");
                    }
                });

        // Actualizar los datos
        existingCustomer.setName(customer.getName());
        existingCustomer.setDocument(customer.getDocument());
        existingCustomer.setPhone(customer.getPhone());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = findById(id);
        customerRepository.deleteById(id);
    }

    public Customer findByDocument(String document) {
        return customerRepository.findByDocument(document)
                .orElseThrow(() -> new CustomerException("No se encontró un cliente con el documento: " + document));
    }
}