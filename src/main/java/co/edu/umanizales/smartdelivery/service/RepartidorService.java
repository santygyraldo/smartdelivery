package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.exception.RepartidorException;
import co.edu.umanizales.smartdelivery.model.Repartidor;
import co.edu.umanizales.smartdelivery.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidores() {
        return repartidorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Repartidor buscarPorId(Long id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new RepartidorException("Repartidor no encontrado con ID: " + id));
    }

    @Transactional
    public Repartidor registrarRepartidor(Repartidor repartidor) {
        if (repartidorRepository.existsByDocumento(repartidor.getDocumento())) {
            throw new RepartidorException("El documento ya está registrado");
        }
        if (repartidorRepository.existsByPlacaVehiculo(repartidor.getPlacaVehiculo())) {
            throw new RepartidorException("La placa del vehículo ya está en uso");
        }
        return repartidorRepository.save(repartidor);
    }

    @Transactional
    public Repartidor actualizarRepartidor(Long id, Repartidor repartidorActualizado) {
        Repartidor repartidorExistente = buscarPorId(id);
        
        if (repartidorRepository.existsByDocumentoAndIdNot(repartidorActualizado.getDocumento(), id)) {
            throw new RepartidorException("El documento ya está en uso por otro repartidor");
        }
        
        if (repartidorRepository.existsByPlacaVehiculoAndIdNot(repartidorActualizado.getPlacaVehiculo(), id)) {
            throw new RepartidorException("La placa del vehículo ya está en uso por otro repartidor");
        }
        
        repartidorExistente.setNombre(repartidorActualizado.getNombre());
        repartidorExistente.setDocumento(repartidorActualizado.getDocumento());
        repartidorExistente.setPlacaVehiculo(repartidorActualizado.getPlacaVehiculo());
        repartidorExistente.setTelefono(repartidorActualizado.getTelefono());
        repartidorExistente.setDisponible(repartidorActualizado.isDisponible());
        
        return repartidorRepository.save(repartidorExistente);
    }

    @Transactional
    public void eliminarRepartidor(Long id) {
        Repartidor repartidor = buscarPorId(id);
        repartidorRepository.delete(repartidor);
    }

    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidoresDisponibles(boolean disponible) {
        return repartidorRepository.findByDisponible(disponible);
    }
}
