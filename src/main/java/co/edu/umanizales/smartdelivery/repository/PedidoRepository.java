package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Pedido;
import co.edu.umanizales.smartdelivery.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByRepartidorId(Long repartidorId);
    List<Pedido> findByEstado(EstadoPedido estado);
    boolean existsByPaqueteId(Long paqueteId);
}
