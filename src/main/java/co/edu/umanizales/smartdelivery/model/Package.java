package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor // Constructor sin argumentos
public class Package {
    
    @CsvBindByName(column = "ID")
    private Long id; // Identificador único del paquete

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @CsvBindByName(column = "WEIGHT")
    private BigDecimal weight;

    @NotNull(message = "La fecha de registro del paquete es obligatoria")
    @CsvBindByName(column = "REGISTRATION_DATE")
    @CsvDate("yyyy-MM-dd")
    @JsonProperty("registrationDate")
    private LocalDate registrationDate;

    @NotBlank(message = "EL nombre del Destinatario es Obligatorio")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @CsvBindByName(column = "RECIPIENTNAME")
    private String recipientName;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @CsvBindByName(column = "DESCRIPTION")
    private String description;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    @CsvBindByName(column = "DELIVERYADDRESS")
    private String deliveryAddress;

}
