package co.edu.umanizales.smartdelivery.service;

import co.edu.umanizales.smartdelivery.exception.ClienteException;
import co.edu.umanizales.smartdelivery.model.Cliente;
import co.edu.umanizales.smartdelivery.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de clientes en el sistema.
 * Maneja la lógica de negocio relacionada con las operaciones CRUD de clientes.
 */
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return Lista de clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id ID del cliente a buscar
     * @return El cliente encontrado
     * @throws ClienteException Si no se encuentra el cliente
     */
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteException("Cliente no encontrado con ID: " + id));
    }

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param cliente Datos del cliente a registrar
     * @return El cliente registrado
     * @throws ClienteException Si el correo electrónico ya está en uso
     */
    @Transactional
    public Cliente registrarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ClienteException("El correo electrónico ya está en uso");
        }
        return clienteRepository.save(cliente);
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id ID del cliente a actualizar
     * @param cliente Datos actualizados del cliente
     * @return El cliente actualizado
     * @throws ClienteException Si el cliente no existe o el correo ya está en uso
     */
    @Transactional
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = buscarPorId(id);
        
        // Verificar si el nuevo correo ya está en uso por otro cliente
        if (clienteRepository.existsByEmail(cliente.getEmail()) && 
            !clienteExistente.getEmail().equals(cliente.getEmail())) {
            throw new ClienteException("El correo electrónico ya está en uso");
        }
        
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setTelefono(cliente.getTelefono());
        
        return clienteRepository.save(clienteExistente);
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id ID del cliente a eliminar
     * @throws ClienteException Si el cliente no existe
     */
    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param email Correo electrónico del cliente a buscar
     * @return El cliente encontrado
     * @throws ClienteException Si no se encuentra el cliente con el correo especificado
     */
    @Transactional(readOnly = true)
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ClienteException("Cliente no encontrado con el correo: " + email));
    }
}
