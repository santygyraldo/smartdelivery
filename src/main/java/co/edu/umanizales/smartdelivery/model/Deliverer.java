package co.edu.umanizales.smartdelivery.model;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Deliverer extends Employee {


    @NotBlank(message = "La placa del vehículo es obligatoria")
    private String vehiclePlate;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    private String phone;


    private boolean available = true;

    public Deliverer(String name, String document, String vehiclePlate, String phone) {
        super(name, document);
        this.vehiclePlate = vehiclePlate;
        this.phone = phone;
    }
}
