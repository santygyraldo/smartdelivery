package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.model.*;
import co.edu.umanizales.smartdelivery.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RepartidorService repartidorService;
    private final PaqueteService paqueteService;
    private final ClienteService clienteService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                        RepartidorService repartidorService,
                        PaqueteService paqueteService,
                        ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.repartidorService = repartidorService;
        this.paqueteService = paqueteService;
        this.clienteService = clienteService;
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        // Cargar el paquete existente
        Paquete paquete = paqueteService.buscarPorId(pedido.getPaquete().getId());
        
        // Cargar el cliente existente
        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        
        // Crear una nueva instancia de Pedido con los datos recibidos
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setPaquete(paquete);
        nuevoPedido.setDireccionEntrega(pedido.getDireccionEntrega());
        
        // Si se asigna un repartidor, verificar que existe y está disponible
        if (pedido.getRepartidor() != null && pedido.getRepartidor().getId() != null) {
            Repartidor repartidor = repartidorService.buscarPorId(pedido.getRepartidor().getId());
            if (!repartidor.isDisponible()) {
                throw new IllegalStateException("El repartidor no está disponible");
            }
            nuevoPedido.setRepartidor(repartidor);
            nuevoPedido.setEstado(EstadoPedido.EN_CAMINO);
        } else {
            nuevoPedido.setEstado(EstadoPedido.PENDIENTE);
        }
        
        return pedidoRepository.save(nuevoPedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));
    }

    @Transactional
    public Pedido actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.actualizarEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido asignarRepartidor(Long pedidoId, Long repartidorId) {
        // Buscar el pedido
        Pedido pedido = buscarPorId(pedidoId);
        
        // Buscar el repartidor
        Repartidor repartidor = repartidorService.buscarPorId(repartidorId);
        
        // Verificar que el repartidor esté disponible
        if (!repartidor.isDisponible()) {
            throw new IllegalStateException("El repartidor no está disponible");
        }
        
        // Verificar que el pedido no esté ya asignado a otro repartidor
        if (pedido.getRepartidor() != null && !pedido.getRepartidor().getId().equals(repartidorId)) {
            throw new IllegalStateException("El pedido ya tiene asignado un repartidor diferente");
        }
        
        // Asignar el repartidor y actualizar el estado
        pedido.setRepartidor(repartidor);
        pedido.actualizarEstado(EstadoPedido.EN_CAMINO);
        
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorRepartidor(Long repartidorId) {
        return pedidoRepository.findByRepartidorId(repartidorId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya entregado");
        }
        pedido.actualizarEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
