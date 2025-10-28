package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "repartidores")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Repartidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "La placa del vehículo es obligatoria")
    @Column(nullable = false, length = 10, unique = true)
    private String placaVehiculo;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    @Column(nullable = false, length = 10)
    private String telefono;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean disponible = true;
    
    public Repartidor(String nombre, String placaVehiculo, String telefono) {
        this.nombre = nombre;
        this.placaVehiculo = placaVehiculo;
        this.telefono = telefono;
    }
}
