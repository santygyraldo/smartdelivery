package co.edu.umanizales.smartdelivery.dto;

import co.edu.umanizales.smartdelivery.model.Package;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long customerId;
    
    @NotNull(message = "La dirección de entrega es obligatoria")
    private String deliveryAddress;
    
    @NotNull(message = "El paquete es obligatorio")
    private Package package_;
    
    private String status = "PENDING";
}
