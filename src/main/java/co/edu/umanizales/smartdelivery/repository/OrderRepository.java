package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.OrderStatus;
import co.edu.umanizales.smartdelivery.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class OrderRepository extends CsvService<Order> {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Override
    protected String getCsvFileName() {
        String fileName = "orders.csv";
        logger.info("Usando archivo CSV: {}", fileName);
        return fileName;
    }
    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }

    @Override
    public List<Order> findAll() {
        try {
            List<Order> orders = super.findAll();
            logger.info("Se encontraron {} órdenes", orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("Error al recuperar todas las órdenes", e);
            throw new RuntimeException("Error al recuperar las órdenes: " + e.getMessage(), e);
        }
    }

    public Optional<Order> findById(Long id) {
        if (id == null) {
            logger.warn("Se intentó buscar una orden con ID nulo");
            return Optional.empty();
        }
        
        try {
            Optional<Order> order = findAll().stream()
                    .filter(p -> p.getId() != null && p.getId().equals(id))
                    .findFirst();
            
            if (order.isPresent()) {
                logger.debug("Orden encontrada con ID: {}", id);
            } else {
                logger.debug("No se encontró ninguna orden con ID: {}", id);
            }
            
            return order;
        } catch (Exception e) {
            logger.error("Error al buscar la orden con ID: " + id, e);
            throw new RuntimeException("Error al buscar la orden: " + e.getMessage(), e);
        }
    }

    public Order update(Order order) {
        if (order == null) {
            logger.error("No se puede actualizar una orden nula");
            throw new IllegalArgumentException("La orden no puede ser nula");
        }
        
        try {
            List<Order> items = findAll();
            
            // Si la orden no tiene ID, asignarle uno nuevo (crear nueva orden)
            if (order.getId() == null) {
                // Generar un nuevo ID (máximo ID existente + 1, o 1 si no hay órdenes)
                Long newId = items.stream()
                        .mapToLong(Order::getId)
                        .max()
                        .orElse(0L) + 1;
                order.setId(newId);
                logger.info("Nueva orden creada con ID: {}", newId);
            } else {
                // Si la orden tiene ID, verificar si existe para actualizarla
                boolean exists = items.removeIf(p -> order.getId().equals(p.getId()));
                if (!exists) {
                    logger.warn("No se encontró ninguna orden con ID: {} para actualizar", order.getId());
                    throw new NoSuchElementException("No se encontró la orden con ID: " + order.getId());
                }
                logger.info("Orden actualizada exitosamente con ID: {}", order.getId());
            }
            
            // Actualizar la fecha de modificación
            order.setUpdateDate(java.time.LocalDateTime.now());
            
            // Guardar la orden (nueva o actualizada)
            items.add(order);
            saveAll(items);
            
            return order;
        } catch (Exception e) {
            logger.error("Error al actualizar la orden con ID: " + order.getId(), e);
            throw new RuntimeException("Error al actualizar la orden: " + e.getMessage(), e);
        }
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            logger.warn("Se intentó eliminar una orden con ID nulo");
            return false;
        }
        
        try {
            List<Order> items = findAll();
            boolean removed = items.removeIf(p -> id.equals(p.getId()));
            
            if (removed) {
                saveAll(items);
                logger.info("Orden con ID: {} eliminada exitosamente", id);
            } else {
                logger.warn("No se encontró ninguna orden con ID: {} para eliminar", id);
            }
            
            return removed;
        } catch (Exception e) {
            logger.error("Error al eliminar la orden con ID: " + id, e);
            throw new RuntimeException("Error al eliminar la orden: " + e.getMessage(), e);
        }
    }

    public List<Order> findByCustomerId(Long customerId) {
        return findAll().stream()
                .filter(p -> p.getCustomer().getId().equals(customerId))
                .toList();
    }

    public List<Order> findByDelivererId(Long delivererId) {
        return findAll().stream()
                .filter(p -> p.getDeliverer().getId().equals(delivererId))
                .toList();
    }

    public List<Order> findByStatus(OrderStatus status) {
        return findAll().stream()
                .filter(p -> p.getStatus().equals(status))
                .toList();
    }

    public boolean existsByPackageId(Long packageId) {
        return findAll().stream()
                .anyMatch(p -> p.getPackage_().getId().equals(packageId));
    }
}
