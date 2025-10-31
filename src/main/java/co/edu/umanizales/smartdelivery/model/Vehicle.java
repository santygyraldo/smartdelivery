package co.edu.umanizales.smartdelivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Vehicle {


    private Long id;


    @NotBlank(message = "La placa es obligatoria")
    @Size(min = 6, max = 10, message = "La placa debe tener entre 6 y 10 caracteres")
    private String plate;

    public Vehicle() {
        // Constructor vacío
    }

    public Vehicle(String plate) {
        this.plate = plate;
    }

    public abstract String getVehicleType();
}
