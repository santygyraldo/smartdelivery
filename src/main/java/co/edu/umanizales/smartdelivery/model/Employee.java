package co.edu.umanizales.smartdelivery.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Employee {  // Clase abstracta no se puede instanciar directamente


    private Long id;


    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;


    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    private String document;

    public Employee() {
        // Constructor vacío
    }
// Constructor con parámetros
    public Employee(String name, String document) {
        this.name = name;
        this.document = document;
    }
}
