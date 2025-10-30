package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "repartidores")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Repartidor extends Empleado {  // aqui esta hereando los atributos de empleado

    @NotBlank(message = "La placa del vehículo es obligatoria")
    @Column(nullable = false, length = 10, unique = true)
    private String placaVehiculo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    @Column(nullable = false, length = 10)
    private String telefono;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean disponible = true;

    public Repartidor() {
        // Constructor vacío requerido por JPA
    }

    public Repartidor(String nombre, String documento, String placaVehiculo, String telefono) {
        super(nombre, documento);
        this.placaVehiculo = placaVehiculo;
        this.telefono = telefono;
    }
}