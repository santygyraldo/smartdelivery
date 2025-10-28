package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Repartidor;
import co.edu.umanizales.smartdelivery.repository.RepartidorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepartidorService {

    private final RepartidorRepository repartidorRepository;

    @Autowired
    public RepartidorService(RepartidorRepository repartidorRepository) {
        this.repartidorRepository = repartidorRepository;
    }

    @Transactional
    public Repartidor registrarRepartidor(Repartidor repartidor) {
        if (repartidorRepository.existsByPlacaVehiculo(repartidor.getPlacaVehiculo())) {
            throw new EntityExistsException("Ya existe un repartidor con la placa: " + repartidor.getPlacaVehiculo());
        }
        if (repartidorRepository.existsByTelefono(repartidor.getTelefono())) {
            throw new EntityExistsException("El teléfono ya está registrado");
        }
        return repartidorRepository.save(repartidor);
    }

    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidores() {
        return repartidorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Repartidor buscarPorId(Long id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Repartidor no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidoresDisponibles() {
        return repartidorRepository.findByDisponible(true);
    }

    @Transactional
    public Repartidor actualizarRepartidor(Long id, Repartidor repartidorActualizado) {
        Repartidor repartidor = buscarPorId(id);
        
        // Verificar si la placa ya está en uso por otro repartidor
        if (!repartidor.getPlacaVehiculo().equals(repartidorActualizado.getPlacaVehiculo()) && 
            repartidorRepository.existsByPlacaVehiculo(repartidorActualizado.getPlacaVehiculo())) {
            throw new EntityExistsException("La placa ya está en uso por otro repartidor");
        }
        
        // Verificar si el teléfono ya está en uso por otro repartidor
        if (!repartidor.getTelefono().equals(repartidorActualizado.getTelefono()) && 
            repartidorRepository.existsByTelefono(repartidorActualizado.getTelefono())) {
            throw new EntityExistsException("El teléfono ya está registrado");
        }
        
        repartidor.setNombre(repartidorActualizado.getNombre());
        repartidor.setPlacaVehiculo(repartidorActualizado.getPlacaVehiculo());
        repartidor.setTelefono(repartidorActualizado.getTelefono());
        repartidor.setDisponible(repartidorActualizado.isDisponible());
        
        return repartidorRepository.save(repartidor);
    }

    @Transactional
    public void eliminarRepartidor(Long id) {
        Repartidor repartidor = buscarPorId(id);
        // Aquí podrías agregar validaciones adicionales, como verificar si tiene pedidos asignados
        repartidorRepository.delete(repartidor);
    }
}
