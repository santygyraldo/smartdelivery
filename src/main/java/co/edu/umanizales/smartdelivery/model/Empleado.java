package co.edu.umanizales.smartdelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase abstracta que representa a un empleado en el sistema.
 * Esta clase sirve como base para diferentes tipos de empleados.
 */
@Getter
@Setter
@MappedSuperclass  //indica que es una superclase para entidades
public abstract class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    // Constructor vacío requerido por JPA
    public Empleado() {
    }

    // Constructor con parámetros
    public Empleado(String nombre, String documento) {
        this.nombre = nombre;
        this.documento = documento;
    }
}
