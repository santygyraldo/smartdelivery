package co.edu.umanizales.smartdelivery.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class Order {
    

    private Long id;
    

    @NotNull(message = "El cliente es obligatorio")
    @ToString.Exclude
    private Customer customer;
    

    @NotNull(message = "La dirección de entrega es obligatoria")
    private String deliveryAddress;
    

    @NotNull(message = "El paquete es obligatorio")
    @ToString.Exclude
    private Package package_;
    

    @ToString.Exclude
    private Deliverer deliverer;
    

    private OrderStatus status = OrderStatus.PENDING;
    

    @PastOrPresent // La fecha de creación debe ser la actual o pasada
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS") // Formato de fecha para CSV
    @CsvBindByName(column = "CREATIONDATE") // Nombre de la columna en el CSV
    private LocalDateTime creationDate = LocalDateTime.now();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    @CsvBindByName(column = "UPDATEDATE")
    private LocalDateTime updateDate;

    // Constructor con parámetros
    public Order(Customer customer, String deliveryAddress, Package package_) {
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.package_ = package_;
    }
    // Actualiza el estado del pedido
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        this.updateDate = LocalDateTime.now();
    }
}
