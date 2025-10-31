package co.edu.umanizales.smartdelivery.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Package {
    
    private Long id;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;
    
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    private BigDecimal weight;
    
    @NotNull(message = "El ancho es obligatorio")
    @DecimalMin(value = "0.1", message = "El ancho debe ser mayor a 0")
    private BigDecimal width;
    
    @NotNull(message = "El alto es obligatorio")
    @DecimalMin(value = "0.1", message = "El alto debe ser mayor a 0")
    private BigDecimal height;
    
    @NotNull(message = "El largo es obligatorio")
    @DecimalMin(value = "0.1", message = "El largo debe ser mayor a 0")
    private BigDecimal length;
    
    private String observations;
    
    // Constructor para nuevos paquetes (sin ID)
    public Package(String description, BigDecimal weight, BigDecimal width, 
                  BigDecimal height, BigDecimal length) {
        this(null, description, weight, width, height, length, null);
    }
    
    // Constructor completo
    public Package(Long id, String description, BigDecimal weight, BigDecimal width, 
                  BigDecimal height, BigDecimal length, String observations) {
        this.id = id;
        this.description = description;
        this.weight = weight;
        this.width = width;
        this.height = height;
        this.length = length;
        this.observations = observations;
    }
}
