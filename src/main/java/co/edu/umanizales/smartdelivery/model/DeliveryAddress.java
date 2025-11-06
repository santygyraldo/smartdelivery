package co.edu.umanizales.smartdelivery.model;

public record DeliveryAddress( // registro inmutable
    String address,
    String city,
    String postalCode,
    String instructions
) {}
// Características principales de record:
//Inmutabilidad:
//Todos los campos son final por defecto
//No se pueden modificar después de la creación
//Código generado automáticamente:
//Constructor con todos los campos
//Métodos de acceso (getters) con el mismo nombre del campo
//equals() y hashCode()
//toString()