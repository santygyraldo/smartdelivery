package co.edu.umanizales.smartdelivery.model;

public record DireccionEntrega(
    String direccion,
    String ciudad,
    String codigoPostal,
    String indicaciones
) {}
