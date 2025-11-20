package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.*;
import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Incluye solo campos no nulos en el JSON
public class Order {

    public enum OrderStatus { REGISTERED, DELIVERED }
    
    @CsvBindByName(column = "ID")
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @JsonProperty("customer")  // Nombre del campo en el JSON
    private Customer customer;

    @NotNull(message = "El paquete es obligatorio")
    @JsonProperty("package")  // Nombre del campo en el JSON
    private Package orderPackage;

    @JsonProperty("deliverer")  // Opcional, solo si se asigna un repartidor
    private Deliverer deliverer;

    @NotNull(message = "El estado es obligatorio")
    @CsvBindByName(column = "STATUS")
    @JsonProperty("status")
    private OrderStatus status = OrderStatus.REGISTERED;

    @JsonProperty("registrationDate")
    private LocalDate registrationDate; // Fecha de registro del pedido

    @JsonProperty("deliveryDate")
    private LocalDate deliveryDate; // Fecha en que se entregó el pedido

}
