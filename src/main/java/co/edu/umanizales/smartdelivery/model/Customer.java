package co.edu.umanizales.smartdelivery.model;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @CsvBindByName(column = "ID", required = true)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @CsvBindByName(column = "NOMBRE", required = true)
    private String name;
    
    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "El documento debe contener entre 6 y 20 dígitos")
    @CsvBindByName(column = "DOCUMENTO", required = true)
    private String document;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    @CsvBindByName(column = "TELEFONO", required = true)
    private String phone;

    public Customer(String name, String document, String phone) {
        this.name = name;
        this.document = document;
        this.phone = phone;
    }
}
