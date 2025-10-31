/**
 * Repositorio para la entidad Customer que proporciona operaciones CRUD básicas
 * y consultas personalizadas relacionadas con la entidad Customer.
 * La persistencia se realiza en un archivo CSV.
 */
package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.service.CsvService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepository extends CsvService<Customer> {

    @Override
    protected String getCsvFileName() {
        return "customers.csv";
    }

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }

    /**
     * Busca un cliente por su número de documento.
     *
     * @param document Número de documento a buscar
     * @return Optional con el cliente encontrado, o vacío si no se encuentra
     */
    public Optional<Customer> findByDocument(String document) {
        return findAll().stream()
                .filter(c -> c.getDocument() != null && c.getDocument().equalsIgnoreCase(document))
                .findFirst();
    }

    public Optional<Customer> findById(Long id) {
        return findAll().stream()
                .filter(c -> c.getId() != null && c.getId().equals(id))
                .findFirst();
    }

    public void update(Customer customer) {
        var items = findAll();
        items.removeIf(c -> c.getId().equals(customer.getId()));
        items.add(customer);
        saveAll(items);
    }

    public void deleteById(Long id) {
        var items = findAll();
        items.removeIf(c -> c.getId().equals(id));
        saveAll(items);
    }
}
