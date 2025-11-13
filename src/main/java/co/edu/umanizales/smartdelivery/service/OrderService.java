package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public OrderService(CustomerService customerService, PackageService packageService, DelivererService delivererService) {
        this.customerService = customerService;
        this.packageService = packageService;
        this.delivererService = delivererService;
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
                return o;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
    }

    public void delete(Long id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(id)) {
                orders.remove(i);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
    }

    public Order assignDeliverer(Long orderId, Long delivererId) {
        Order o = findById(orderId);
        delivererService.findById(delivererId);
        o.setDelivererId(delivererId);
        return o;
    }

    public Order unassignDeliverer(Long orderId) {
        Order o = findById(orderId);
        o.setDelivererId(null);
        return o;
    }
}
