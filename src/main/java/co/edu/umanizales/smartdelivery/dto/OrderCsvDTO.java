package co.edu.umanizales.smartdelivery.dto;

import com.opencsv.bean.CsvBindByName;

public class OrderCsvDTO {
    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "CUSTOMER_ID")
    private Long customerId;

    @CsvBindByName(column = "PACKAGE_ID")
    private Long packageId;

    @CsvBindByName(column = "DELIVERER_ID")
    private Long delivererId;

    @CsvBindByName(column = "STATUS")
    private String status;

    @CsvBindByName(column = "REGISTRATION_DATE")
    private String registrationDate;

    @CsvBindByName(column = "DELIVERY_DATE")
    private String deliveryDate;

    public OrderCsvDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }

    public Long getDelivererId() { return delivererId; }
    public void setDelivererId(Long delivererId) { this.delivererId = delivererId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
}
