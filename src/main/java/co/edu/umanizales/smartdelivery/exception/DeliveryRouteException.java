package co.edu.umanizales.smartdelivery.exception;

/**
 * Excepción personalizada para manejar errores específicos de las rutas de entrega.
 * Se lanza cuando ocurre un error relacionado con las operaciones de rutas de entrega.
 */
public class DeliveryRouteException extends RuntimeException {

    /**
     * Construye una nueva excepción con el mensaje de error especificado.
     *
     * @param message el mensaje de error detallado
     */
    public DeliveryRouteException(String message) {
        super(message);
    }

    /**
     * Construye una nueva excepción con el mensaje de error especificado y la causa.
     *
     * @param message el mensaje de error detallado
     * @param cause la causa de la excepción
     */
    public DeliveryRouteException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construye una nueva excepción con la causa especificada.
     *
     * @param cause la causa de la excepción
     */
    public DeliveryRouteException(Throwable cause) {
        super(cause);
    }
}
