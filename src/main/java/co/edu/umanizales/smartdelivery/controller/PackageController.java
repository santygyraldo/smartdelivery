package co.edu.umanizales.smartdelivery.controller;

import co.edu.umanizales.smartdelivery.model.Package;
import co.edu.umanizales.smartdelivery.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    public ResponseEntity<Package> createPackage(@Valid @RequestBody Package package_) {
        Package newPackage = packageService.savePackage(package_);
        return new ResponseEntity<>(newPackage, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Package>> listPackages() {
        return ResponseEntity.ok(packageService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackage(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}
