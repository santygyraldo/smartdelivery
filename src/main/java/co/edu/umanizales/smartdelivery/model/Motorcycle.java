package co.edu.umanizales.smartdelivery.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Motorcycle extends Vehicle {

    public Motorcycle() {
        // Constructor vacío
    }

    public Motorcycle(String plate) {
        super(plate);
    }

    @Override
    public String getVehicleType() {
        return "Motorcycle";
    }
}
