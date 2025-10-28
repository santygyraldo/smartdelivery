package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "paquetes")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Paquete {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Column(nullable = false, length = 255)
    private String descripcion;
    
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal peso; // en kilogramos
    
    @NotNull(message = "El ancho es obligatorio")
    @DecimalMin(value = "0.1", message = "El ancho debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ancho; // en centímetros
    
    @NotNull(message = "El alto es obligatorio")
    @DecimalMin(value = "0.1", message = "El alto debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal alto; // en centímetros
    
    @NotNull(message = "El largo es obligatorio")
    @DecimalMin(value = "0.1", message = "El largo debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal largo; // en centímetros
    
    @Column(length = 500)
    private String observaciones;
    
    public Paquete(String descripcion, BigDecimal peso, BigDecimal ancho, 
                  BigDecimal alto, BigDecimal largo) {
        this.descripcion = descripcion;
        this.peso = peso;
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
    }
}
