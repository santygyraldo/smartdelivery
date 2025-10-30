package co.edu.umanizales.smartdelivery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "camiones")
@PrimaryKeyJoinColumn(name = "vehiculo_id")
public class Camion extends Vehiculo {

    public Camion() {
        // Constructor vacío requerido por JPA
    }

    public Camion(String placa) {
        super(placa);
    }

    @Override
    public String getTipoVehiculo() {
        return "Camión";
    }
}