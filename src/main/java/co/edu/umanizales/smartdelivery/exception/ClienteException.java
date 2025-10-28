package co.edu.umanizales.smartdelivery.exception;

/**
 * Excepción personalizada para manejar errores relacionados con la gestión de clientes.
 */
public class ClienteException extends RuntimeException {
    
    public ClienteException(String message) {
        super(message);
    }
    
    public ClienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
