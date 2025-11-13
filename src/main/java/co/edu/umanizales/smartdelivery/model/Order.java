package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.*;
import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Incluye solo campos no nulos en el JSON
public class Order {
    
    @CsvBindByName(column = "ID")
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @CsvBindByName(column = "CUSTOMER_ID")
    @JsonProperty("customerId")  // Nombre del campo en el JSON
    private Long customerId;

    @NotNull(message = "El paquete es obligatorio")
    @CsvBindByName(column = "PACKAGE_ID")
    @JsonProperty("packageId")  // Nombre del campo en el JSON
    private Long packageId;

    @CsvBindByName(column = "DELIVERER_ID")
    @JsonProperty("delivererId")  // Opcional, solo si se asigna un repartidor
    private Long delivererId;


    @NotNull(message = "La dirección de entrega es obligatoria")
    @CsvBindByName(column = "DELIVERY_ADDRESS")
    private String deliveryAddress;


    }

