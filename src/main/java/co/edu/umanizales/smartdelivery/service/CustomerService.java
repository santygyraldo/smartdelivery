package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import co.edu.umanizales.smartdelivery.service.CsvService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>(); //Arraylist significa que es una lista dinamica
    private final AtomicLong idSequence = new AtomicLong(1); // atomicLong significa que es un numero que se incrementa automaticamente
    private final CsvService csvService;

    public CustomerService(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostConstruct
    private void loadFromCsvAtStartup() {
        try {
            Path file = Paths.get("data").resolve("customers.csv");
            if (!Files.exists(file)) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {
                CsvToBean<Customer> csvToBean = new CsvToBeanBuilder<Customer>(reader)
                        .withType(Customer.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();
                List<Customer> loaded = csvToBean.parse();
                customers.clear();
                customers.addAll(loaded);
                long maxId = loaded.stream()
                        .filter(c -> c.getId() != null)
                        .mapToLong(Customer::getId)
                        .max()
                        .orElse(0L);
                idSequence.set(maxId + 1);
            }
        } catch (Exception ignored) {
        }
    }

    public Customer create(Customer customer) { // Crea un cliente
        if (customer.getId() == null) {
            customer.setId(idSequence.getAndIncrement()); //idSequence.getAndIncrement() significa que el id se incrementa automaticamente
        }
        if (existsByDocument(customer.getDocument())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya existe"); //HttpStatus.BAD_REQUEST significa que el documento ya existe
        }
        customers.add(customer);
        csvService.exportCustomers(customers);
        return customer;
    }

    public List<Customer> findAll() {
        return customers;
    }

    public Customer findById(Long id) { // Busca un cliente por su ID
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                csvService.exportCustomers(customers);
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
                csvService.exportCustomers(customers);
                return c;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    public void delete(Long id) { // Elimina un cliente por su ID
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(id)) {
                customers.remove(i);
                csvService.exportCustomers(customers);
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
