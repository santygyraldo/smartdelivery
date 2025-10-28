package co.edu.umanizales.smartdelivery.repository;

import co.edu.umanizales.smartdelivery.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Cliente que proporciona operaciones CRUD básicas
 * y consultas personalizadas relacionadas con la entidad Cliente.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su dirección de correo electrónico.
     *
     * @param email la dirección de correo electrónico del cliente a buscar
     * @return un Optional que contiene el cliente si se encuentra, o vacío si no
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Verifica si existe un cliente con la dirección de correo electrónico especificada.
     *
     * @param email la dirección de correo electrónico a verificar
     * @return true si existe un cliente con el correo electrónico, false en caso contrario
     */
    boolean existsByEmail(String email);
}
