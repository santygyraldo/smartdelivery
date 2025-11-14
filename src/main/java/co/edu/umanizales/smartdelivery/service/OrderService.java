package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Order;
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
        customerService.findById(order.getCustomerId());
        packageService.findById(order.getPackageId());
        if (order.getDelivererId() != null) {
            delivererService.findById(order.getDelivererId());
        }
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
                if (update.getCustomerId() != null) {
                    customerService.findById(update.getCustomerId());
                    o.setCustomerId(update.getCustomerId());
                }
                if (update.getPackageId() != null) {
                    packageService.findById(update.getPackageId());
                    o.setPackageId(update.getPackageId());
                }
                if (update.getDelivererId() != null) {
                    delivererService.findById(update.getDelivererId());
                    o.setDelivererId(update.getDelivererId());
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
        delivererService.findById(delivererId);
        o.setDelivererId(delivererId);
        csvService.exportOrders(orders);
        return o;
    }

    public Order unassignDeliverer(Long orderId) {
        Order o = findById(orderId);
        o.setDelivererId(null);
        csvService.exportOrders(orders);
        return o;
    }

    public void loadAll(List<Order> orders) {
    }
}
