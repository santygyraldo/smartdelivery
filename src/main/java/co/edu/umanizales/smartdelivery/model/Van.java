package co.edu.umanizales.smartdelivery.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Van extends Vehicle {

    public Van() {
        // Constructor vacío
    }

    public Van(String plate) {
        super(plate);
    }

    @Override
    public String getVehicleType() {
        return "Van";
    }
}
