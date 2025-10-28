package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    List<Repartidor> findByDisponible(boolean disponible);
    boolean existsByPlacaVehiculo(String placaVehiculo);
    boolean existsByTelefono(String telefono);
}
