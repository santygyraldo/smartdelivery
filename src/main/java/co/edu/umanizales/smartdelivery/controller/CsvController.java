package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Customer;
import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.model.Order;
import co.edu.umanizales.smartdelivery.model.Vehicle;
import co.edu.umanizales.smartdelivery.service.CsvService;
import co.edu.umanizales.smartdelivery.service.CustomerService;
import co.edu.umanizales.smartdelivery.service.PackageService;
import co.edu.umanizales.smartdelivery.service.DelivererService;
import co.edu.umanizales.smartdelivery.service.OrderService;
import co.edu.umanizales.smartdelivery.service.VehicleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/csv")
public class CsvController {

    private final CsvService csvService;
    private final CustomerService customerService;
    private final PackageService packageService;
    private final DelivererService delivererService;
    private final OrderService orderService;
    private final VehicleService vehicleService;

    public CsvController(
            CsvService csvService,
            CustomerService customerService,
            PackageService packageService,
            DelivererService delivererService,
            OrderService orderService,
            VehicleService vehicleService) {
        this.csvService = csvService;
        this.customerService = customerService;
        this.packageService = packageService;
        this.delivererService = delivererService;
        this.orderService = orderService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/customers")
    public Map<String, String> exportCustomers() {
        List<Customer> list = customerService.findAll();
        Path path = csvService.exportCustomers(list); // path es la ruta del archivo
        Map<String, String> res = new HashMap<>();
        res.put("file", path.toString());
        return res;
    }

    @GetMapping("/packages")
    public Map<String, String> exportPackages() {
        List<Package> list = packageService.findAll();
        Path path = csvService.exportPackages(list);
        Map<String, String> res = new HashMap<>();
        res.put("file", path.toString());
        return res;
    }

    @GetMapping("/deliverers")
    public Map<String, String> exportDeliverers() {
        List<Deliverer> list = delivererService.findAll();
        Path path = csvService.exportDeliverers(list);
        Map<String, String> res = new HashMap<>();
        res.put("file", path.toString());
        return res;
    }

    @GetMapping("/orders")
    public Map<String, String> exportOrders() {
        List<Order> list = orderService.findAll();
        Path path = csvService.exportOrders(list);
        Map<String, String> res = new HashMap<>();
        res.put("file", path.toString());
        return res;
    }

    @GetMapping("/vehicles")
    public Map<String, String> exportVehicles() {
        List<Vehicle> list = vehicleService.findAll();
        Path path = csvService.exportVehicles(list);
        Map<String, String> res = new HashMap<>();
        res.put("file", path.toString());
        return res;
    }
}
