package co.edu.umanizales.smartdelivery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data // getters y setters
@EqualsAndHashCode(callSuper = true) // herencia
@Entity  // entidad JPA
@Table(name = "camionetas")
@PrimaryKeyJoinColumn(name = "vehiculo_id")  // herencia
public class Camioneta extends Vehiculo {

    public Camioneta() {
        // Constructor vacío requerido por JPA
    }

    public Camioneta(String placa) {
        super(placa); // herencia Vehiculo
            }

    @Override
    public String getTipoVehiculo() {
        return "Camioneta";
    }
}