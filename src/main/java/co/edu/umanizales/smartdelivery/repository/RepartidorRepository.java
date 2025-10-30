package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    boolean existsByDocumento(String documento);
    boolean existsByPlacaVehiculo(String placaVehiculo);
    boolean existsByDocumentoAndIdNot(String documento, Long id);
    boolean existsByPlacaVehiculoAndIdNot(String placaVehiculo, Long id);
    List<Repartidor> findByDisponible(boolean disponible);
    Optional<Repartidor> findByDocumento(String documento);
}
