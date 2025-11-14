package co.edu.umanizales.smartdelivery.model;


import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.*; // importa las anotaciones de validacion
import lombok.Data; // importa la anotacion Data de lombok
import lombok.NoArgsConstructor; //
import lombok.EqualsAndHashCode;

@Data // genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // genera constructor sin argumentos
@EqualsAndHashCode(callSuper = true) // genera equals y hashCode considerando la superclase
public class Deliverer extends Employee { // extendes hereda de Empleado


    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    private String phone;

    private boolean available = true; // indica si el repartidor esta disponible

    @CsvBindByName(column = "VEHICLE_ID")
    private Long vehicleId; // relacion persistida con el vehículo asignado

    // constructor
    public Deliverer(String name, String document, String vehiclePlate, String phone) {
        super(name, document);
         this.phone = phone;
    }
}
