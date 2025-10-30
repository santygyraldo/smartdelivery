package co.edu.umanizales.smartdelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity // entidad JPA
@Table(name = "rutas_entrega")
public class RutaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // autoincremental
    private Long id;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 200, message = "El origen no puede tener más de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 200, message = "El destino no puede tener más de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String destino;

    public RutaEntrega() {
        // Constructor vacío requerido por JPA
    }

    public RutaEntrega(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }
}
