package co.edu.umanizales.smartdelivery.dto;

import com.opencsv.bean.CsvBindByName;

public class DelivererCsvDTO {
    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "NAME")
    private String name;

    @CsvBindByName(column = "DOCUMENT")
    private String document;

    @CsvBindByName(column = "PHONE")
    private String phone;

    @CsvBindByName(column = "AVAILABLE")
    private Boolean available;

    @CsvBindByName(column = "VEHICLE_ID")
    private Long vehicleId;

    public DelivererCsvDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
}
