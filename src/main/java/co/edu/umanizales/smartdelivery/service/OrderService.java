package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.model.Vehicle;
import co.edu.umanizales.smartdelivery.dto.OrderCsvDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
                CsvToBean<OrderCsvDTO> csvToBean = new CsvToBeanBuilder<OrderCsvDTO>(reader)
                        .withType(OrderCsvDTO.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();
                List<OrderCsvDTO> flat = csvToBean.parse();
                orders.clear();
                for (OrderCsvDTO dto : flat) {
                    Order o = new Order();
                    o.setId(dto.getId());
                    if (dto.getCustomerId() != null) {
                        try { o.setCustomer(customerService.findById(dto.getCustomerId())); } catch (ResponseStatusException ignored) {}
                    }
                    if (dto.getPackageId() != null) {
                        try { o.setOrderPackage(packageService.findById(dto.getPackageId())); } catch (ResponseStatusException ignored) {}
                    }
                    if (dto.getDelivererId() != null) {
                        try { o.setDeliverer(delivererService.findById(dto.getDelivererId())); } catch (ResponseStatusException ignored) {}
                    }
                    if (dto.getStatus() != null) {
                        try { o.setStatus(Order.OrderStatus.valueOf(dto.getStatus())); } catch (IllegalArgumentException ignored) {}
                    }
                    if (dto.getRegistrationDate() != null && !dto.getRegistrationDate().isBlank()) {
                        try { o.setRegistrationDate(LocalDate.parse(dto.getRegistrationDate())); } catch (Exception ignored) {}
                    }
                    if (dto.getDeliveryDate() != null && !dto.getDeliveryDate().isBlank()) {
                        try { o.setDeliveryDate(LocalDate.parse(dto.getDeliveryDate())); } catch (Exception ignored) {}
                    }
                    orders.add(o);
                }
                long maxId = orders.stream()
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
        if (order.getRegistrationDate() == null) {
            order.setRegistrationDate(LocalDate.now());
        }
        if (order.getDeliveryDate() != null && order.getRegistrationDate() != null
                && order.getDeliveryDate().isBefore(order.getRegistrationDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de entrega no puede ser menor a la fecha de registro");
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
                // Validar fechas antes de aplicar definitivamente
                LocalDate newReg = update.getRegistrationDate() != null ? update.getRegistrationDate() : o.getRegistrationDate();
                LocalDate newDel = update.getDeliveryDate() != null ? update.getDeliveryDate() : o.getDeliveryDate();
                if (newReg != null && newDel != null && newDel.isBefore(newReg)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de entrega no puede ser menor a la fecha de registro");
                }
                if (update.getRegistrationDate() != null) {
                    o.setRegistrationDate(update.getRegistrationDate());
                }
                if (update.getDeliveryDate() != null) {
                    o.setDeliveryDate(update.getDeliveryDate());
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

    public static class TypeCountDTO {
        private String tipo;
        private long cant;
        public TypeCountDTO() {}
        public TypeCountDTO(String tipo, long cant) { this.tipo = tipo; this.cant = cant; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public long getCant() { return cant; }
        public void setCant(long cant) { this.cant = cant; }
    }

    public static class ReportDayDTO {
        private String fecha;
        private long total;
        private List<TypeCountDTO> tipos_veh;
        public ReportDayDTO() {}
        public ReportDayDTO(String fecha, long total, List<TypeCountDTO> tipos_veh) {
            this.fecha = fecha; this.total = total; this.tipos_veh = tipos_veh;
        }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public List<TypeCountDTO> getTipos_veh() { return tipos_veh; }
        public void setTipos_veh(List<TypeCountDTO> tipos_veh) { this.tipos_veh = tipos_veh; }
    }

    public List<ReportDayDTO> reportDeliveriesByVehicle(LocalDate startDate, LocalDate endDate, String vehicleType) {
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las fechas son obligatorias");
        }
        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha final no puede ser anterior a la inicial");
        }

        Map<LocalDate, Map<String, Long>> byDateThenType = new LinkedHashMap<>();

        for (Order o : orders) {
            if (o.getStatus() != Order.OrderStatus.DELIVERED) continue;
            LocalDate d = o.getDeliveryDate();
            if (d == null) continue;
            if (d.isBefore(startDate) || d.isAfter(endDate)) continue;
            Deliverer del = o.getDeliverer();
            if (del == null) continue;
            Vehicle veh = del.getVehicle();
            if (veh == null) continue;
            String type = veh.getVehicleType();
            if (type == null) continue;
            String normalized = type.toUpperCase();
            if (vehicleType != null && !vehicleType.isBlank() && !normalized.equalsIgnoreCase(vehicleType)) continue;

            byDateThenType.computeIfAbsent(d, k -> new LinkedHashMap<>());
            Map<String, Long> typeMap = byDateThenType.get(d);
            typeMap.put(normalized, typeMap.getOrDefault(normalized, 0L) + 1L);
        }

        List<ReportDayDTO> result = new ArrayList<>();
        byDateThenType.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    LocalDate date = e.getKey();
                    Map<String, Long> typeMap = e.getValue();
                    long total = typeMap.values().stream().mapToLong(Long::longValue).sum();
                    List<TypeCountDTO> list = new ArrayList<>();
                    typeMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(t -> list.add(new TypeCountDTO(t.getKey(), t.getValue())));
                    result.add(new ReportDayDTO(date.toString(), total, list));
                });
        return result;
    }
}
