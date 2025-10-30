package co.edu.umanizales.smartdelivery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data // getters y setters
@EqualsAndHashCode(callSuper = true) // herencia
@Entity // entidad JPA
@Table(name = "motos") // nombre de la tabla
@PrimaryKeyJoinColumn(name = "vehiculo_id") // herencia de Vehiculo
public class Moto extends Vehiculo {

    public Moto() {
        // Constructor vacío requerido por JPA
    }

    public Moto(String placa) {
        super(placa);// herencia de Vehiculo
    }

    @Override   // sobreescribe el metodo de Vehiculo
    public String getTipoVehiculo() {
        return "Moto";
    }
}