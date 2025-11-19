package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // las respuestas de los metodos seran en formato json
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Package create(@Valid @RequestBody Package pkg) {
        return packageService.create(pkg);
    }

    @GetMapping
    public List<Package> findAll() {
        return packageService.findAll();
    }

    @GetMapping("/{id}")
    public Package findById(@PathVariable Long id) {
        return packageService.findById(id);
    }

    @PutMapping("/{id}")
    public Package update(@PathVariable Long id, @Valid @RequestBody Package pkg) {
        return packageService.update(id, pkg);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        packageService.delete(id);
    }
}
