package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    private final CustomerService customerService;
    private final PackageService packageService;
    private final DelivererService delivererService;
    private final CsvService csvService;

    public OrderService(CustomerService customerService, PackageService packageService, DelivererService delivererService, CsvService csvService) {
        this.customerService = customerService;
        this.packageService = packageService;
        this.delivererService = delivererService;
        this.csvService = csvService;
    }

    @PostConstruct
    private void loadFromCsvAtStartup() {
        try {
            Path file = Paths.get("data").resolve("orders.csv");
            if (!Files.exists(file)) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {
                CsvToBean<Order> csvToBean = new CsvToBeanBuilder<Order>(reader)
                        .withType(Order.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();
                List<Order> loaded = csvToBean.parse();
                orders.clear();
                orders.addAll(loaded);
                long maxId = loaded.stream()
                        .filter(o -> o.getId() != null)
                        .mapToLong(Order::getId)
                        .max()
                        .orElse(0L);
                idSequence.set(maxId + 1);
            }
        } catch (Exception ignored) {
        }
    }

    public Order create(Order order) {
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente es obligatorio");
        }
        if (order.getOrderPackage() == null || order.getOrderPackage().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paquete es obligatorio");
        }

        Customer customer = customerService.findById(order.getCustomer().getId());
        Package pkg = packageService.findById(order.getOrderPackage().getId());
        Deliverer deliverer = null;
        if (order.getDeliverer() != null) {
            if (order.getDeliverer().getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El repartidor debe tener ID");
            }
            deliverer = delivererService.findById(order.getDeliverer().getId());
        }

        order.setCustomer(customer);
        order.setOrderPackage(pkg);
        order.setDeliverer(deliverer);

        if (order.getId() == null) {
            order.setId(idSequence.getAndIncrement());
        }
        orders.add(order);
        csvService.exportOrders(orders);
        return order;
    }

    public List<Order> findAll() {
        return orders;
    }

    public Order findById(Long id) {
        for (Order o : orders) {
            if (o.getId().equals(id)) {
                return o;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
    }

    public Order update(Long id, Order update) {
        for (Order o : orders) {
            if (o.getId().equals(id)) {
                if (update.getCustomer() != null) {
                    if (update.getCustomer().getId() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente debe tener ID");
                    }
                    Customer customer = customerService.findById(update.getCustomer().getId());
                    o.setCustomer(customer);
                }
                if (update.getOrderPackage() != null) {
                    if (update.getOrderPackage().getId() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paquete debe tener ID");
                    }
                    Package pkg = packageService.findById(update.getOrderPackage().getId());
                    o.setOrderPackage(pkg);
                }
                if (update.getDeliverer() != null) {
                    if (update.getDeliverer().getId() == null) {
                        o.setDeliverer(null);
                    } else {
                        Deliverer deliverer = delivererService.findById(update.getDeliverer().getId());
                        o.setDeliverer(deliverer);
                    }
                }
                if (update.getStatus() != null) {
                    o.setStatus(update.getStatus());
                }
                csvService.exportOrders(orders);
                return o;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(id)) {
                orders.remove(i);
                csvService.exportOrders(orders);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
    }

    public Order assignDeliverer(Long orderId, Long delivererId) {
        Order o = findById(orderId);
        Deliverer deliverer = delivererService.findById(delivererId);
        o.setDeliverer(deliverer);
        csvService.exportOrders(orders);
        return o;
    }

    public Order unassignDeliverer(Long orderId) {
        Order o = findById(orderId);
        o.setDeliverer(null);
        csvService.exportOrders(orders);
        return o;
    }

    public void loadAll(List<Order> orders) {
    }
}
