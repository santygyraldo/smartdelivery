package co.edu.umanizales.smartdelivery.exception;

/**
 * Excepción personalizada para manejar errores relacionados con la gestión de clientes.
 */
public class CustomerException extends RuntimeException {
    
    public CustomerException(String message) {
        super(message);
    }
    
    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
