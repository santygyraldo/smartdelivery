package co.edu.umanizales.smartdelivery.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data // significa que tiene todos los metodos get y set
@EqualsAndHashCode(callSuper = true) //significa que tiene todos los metodos equals y hashcode
public class Truck extends Vehicle {

    public Truck() {

    }

    public Truck(String plate) {
        super(plate);
    } // significa que llama al constructor de la clase padre

    @Override // significa que esta sobreescrito
    public String getVehicleType() {
        return "Truck";
    }
}
