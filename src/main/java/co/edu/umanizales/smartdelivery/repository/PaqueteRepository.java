package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    // No es necesario agregar métodos adicionales por ahora
}
