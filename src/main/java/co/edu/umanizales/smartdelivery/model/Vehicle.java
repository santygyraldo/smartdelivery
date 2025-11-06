package co.edu.umanizales.smartdelivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase abstracta que representa un vehículo
 */
@Getter
@Setter
public abstract class Vehicle {

    /**
     * Identificador único del vehículo
     */
    private Long id;

    /**
     * Placa del vehículo
     */
    @NotBlank(message = "La placa es obligatoria")
    @Size(min = 6, max = 10, message = "La placa debe tener entre 6 y 10 caracteres")
    private String plate;

    /**
     * Constructor vacío
     */
    public Vehicle() {
    }

    /**
     * Constructor con parámetro
     * @param plate Placa del vehículo
     */
    public Vehicle(String plate) {
        this.plate = plate;
    }

    /**
     * Método abstracto para obtener el tipo de vehículo
     * @return Tipo de vehículo
     */
    public abstract String getVehicleType();
}
