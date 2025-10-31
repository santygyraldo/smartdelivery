package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.exception.DelivererException;
import co.edu.umanizales.smartdelivery.model.Deliverer;
import co.edu.umanizales.smartdelivery.repository.DelivererRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelivererService {

    @Autowired
    private DelivererRepository delivererRepository;

    public List<Deliverer> listDeliverers() {
        return delivererRepository.findAll();
    }

    public Deliverer findById(Long id) {
        return delivererRepository.findById(id)
                .orElseThrow(() -> new DelivererException("Deliverer not found with ID: " + id));
    }

    public Deliverer registerDeliverer(Deliverer deliverer) {
        if (delivererRepository.existsByDocument(deliverer.getDocument())) {
            throw new DelivererException("The document is already registered");
        }
        if (delivererRepository.existsByVehiclePlate(deliverer.getVehiclePlate())) {
            throw new DelivererException("The vehicle plate is already in use");
        }
        delivererRepository.save(deliverer);
        return deliverer;
    }

    public Deliverer updateDeliverer(Long id, Deliverer updatedDeliverer) {
        Deliverer existingDeliverer = findById(id);
        
        if (delivererRepository.existsByDocumentAndIdNot(updatedDeliverer.getDocument(), id)) {
            throw new DelivererException("The document is already in use by another deliverer");
        }
        
        if (delivererRepository.existsByVehiclePlateAndIdNot(updatedDeliverer.getVehiclePlate(), id)) {
            throw new DelivererException("The vehicle plate is already in use by another deliverer");
        }
        
        existingDeliverer.setName(updatedDeliverer.getName());
        existingDeliverer.setDocument(updatedDeliverer.getDocument());
        existingDeliverer.setVehiclePlate(updatedDeliverer.getVehiclePlate());
        existingDeliverer.setPhone(updatedDeliverer.getPhone());
        existingDeliverer.setAvailable(updatedDeliverer.isAvailable());
        
        delivererRepository.save(existingDeliverer);
        return existingDeliverer;
    }

    public void deleteDeliverer(Long id) {
        Deliverer deliverer = findById(id);
        delivererRepository.deleteById(id);
    }

    public List<Deliverer> listAvailableDeliverers(boolean available) {
        return delivererRepository.findByAvailable(available);
    }
}
