package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.OrderStatus;
import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DelivererService delivererService;
    private final PackageService packageService;
    private final CustomerService customerService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        DelivererService delivererService,
                        PackageService packageService,
                        CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.delivererService = delivererService;
        this.packageService = packageService;
        this.customerService = customerService;
    }

    public Order createOrder(Order order) {
        // Obtener o crear el paquete
        Package package_ = order.getPackage_();
        if (package_.getId() == null) {
            // Si el paquete no tiene ID, crearlo
            package_ = packageService.savePackage(package_);
        } else {
            // Si tiene ID, verificar que exista
            try {
                package_ = packageService.findById(package_.getId());
            } catch (NoSuchElementException e) {
                // Si no existe, crear uno nuevo con los datos proporcionados
                package_ = packageService.savePackage(package_);
            }
        }
        
        // Verificar que el cliente existe
        Customer customer = customerService.findById(order.getCustomer().getId());
        if (customer == null) {
            throw new NoSuchElementException("No se encontró el cliente con ID: " + order.getCustomer().getId());
        }
        
        // Crear la nueva orden
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setPackage_(package_);
        newOrder.setDeliveryAddress(order.getDeliveryAddress());
        
        // Asignar repartidor si se proporciona
        if (order.getDeliverer() != null && order.getDeliverer().getId() != null) {
            Deliverer deliverer = delivererService.findById(order.getDeliverer().getId());
            if (!deliverer.isAvailable()) {
                throw new IllegalStateException("El repartidor no está disponible");
            }
            newOrder.setDeliverer(deliverer);
            newOrder.setStatus(OrderStatus.IN_TRANSIT);
        } else {
            newOrder.setStatus(OrderStatus.PENDING);
        }
        
        orderRepository.save(newOrder);
        return newOrder;
    }

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = findById(id);
        order.setStatus(newStatus);
        orderRepository.save(order);
        return order;
    }

    public Order assignDeliverer(Long orderId, Long delivererId) {
        Order order = findById(orderId);
        Deliverer deliverer = delivererService.findById(delivererId);
        
        if (!deliverer.isAvailable()) {
            throw new IllegalStateException("The deliverer is not available");
        }
        
        if (order.getDeliverer() != null && !order.getDeliverer().getId().equals(delivererId)) {
            throw new IllegalStateException("The order already has a different deliverer assigned");
        }
        
        order.setDeliverer(deliverer);
        order.setStatus(OrderStatus.IN_TRANSIT);
        orderRepository.save(order);
        return order;
    }

    public List<Order> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByDeliverer(Long delivererId) {
        return orderRepository.findByDelivererId(delivererId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void cancelOrder(Long id) {
        Order order = findById(id);
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has already been delivered");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
