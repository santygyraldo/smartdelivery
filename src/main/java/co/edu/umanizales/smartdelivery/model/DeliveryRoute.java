package co.edu.umanizales.smartdelivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeliveryRoute {

    private Long id;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 200, message = "El origen no puede tener más de 200 caracteres")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 200, message = "El destino no puede tener más de 200 caracteres")
    private String destination;

    public DeliveryRoute() {
        // Constructor vacío
    }

    public DeliveryRoute(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
