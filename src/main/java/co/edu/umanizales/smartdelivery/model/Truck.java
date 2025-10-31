package co.edu.umanizales.smartdelivery.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Truck extends Vehicle {

    public Truck() {
        // Constructor vacío
    }

    public Truck(String plate) {
        super(plate);
    }

    @Override
    public String getVehicleType() {
        return "Truck";
    }
}
