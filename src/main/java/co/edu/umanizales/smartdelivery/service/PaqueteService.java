package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.Paquete;
import co.edu.umanizales.smartdelivery.repository.PaqueteRepository;
import co.edu.umanizales.smartdelivery.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final PedidoRepository pedidoRepository;

    @Autowired
    public PaqueteService(PaqueteRepository paqueteRepository, PedidoRepository pedidoRepository) {
        this.paqueteRepository = paqueteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Paquete guardarPaquete(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    @Transactional(readOnly = true)
    public List<Paquete> listarTodos() {
        return paqueteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Paquete buscarPorId(Long id) {
        return paqueteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + id));
    }

    @Transactional
    public void eliminarPaquete(Long id) {
        if (pedidoRepository.existsByPaqueteId(id)) {
            throw new IllegalStateException("No se puede eliminar el paquete porque está asociado a un pedido");
        }
        paqueteRepository.deleteById(id);
    }
}
