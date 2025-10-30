package co.edu.umanizales.smartdelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // no espacios vacios
import jakarta.validation.constraints.Size; // longitud minima y maxima
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Vehiculo {

    @Id // identificador unico
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremental la ID
    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Size(min = 6, max = 10, message = "La placa debe tener entre 6 y 10 caracteres")
    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    public Vehiculo() {
        // Constructor vacío requerido por JPA
    }

    public Vehiculo(String placa) {
        this.placa = placa;
    }

    public abstract String getTipoVehiculo();
}