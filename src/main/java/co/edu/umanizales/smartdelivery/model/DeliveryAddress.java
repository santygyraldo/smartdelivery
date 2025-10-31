package co.edu.umanizales.smartdelivery.model;

public record DeliveryAddress( // registro inmutable
    String address,
    String city,
    String postalCode,
    String instructions
) {}
