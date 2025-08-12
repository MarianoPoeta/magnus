package com.magnus.domain;

import com.magnus.domain.enumeration.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Transport catalog and templates
 */
@Entity
@Table(name = "transport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @NotNull
    @Min(value = 1)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_per_hour", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerHour;

    @DecimalMin(value = "0")
    @Column(name = "price_per_km", precision = 21, scale = 2)
    private BigDecimal pricePerKm;

    @DecimalMin(value = "0")
    @Column(name = "cost_per_hour", precision = 21, scale = 2)
    private BigDecimal costPerHour;

    @DecimalMin(value = "0")
    @Column(name = "cost_per_km", precision = 21, scale = 2)
    private BigDecimal costPerKm;

    @NotNull
    @Column(name = "includes_driver", nullable = false)
    private Boolean includesDriver;

    @DecimalMin(value = "0")
    @Column(name = "driver_cost", precision = 21, scale = 2)
    private BigDecimal driverCost;

    @Size(max = 50)
    @Column(name = "fuel_type", length = 50)
    private String fuelType;

    @Lob
    @Column(name = "contact_info")
    private String contactInfo;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_template", nullable = false)
    private Boolean isTemplate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Transport name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Transport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public Transport vehicleType(VehicleType vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Transport capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerHour() {
        return this.pricePerHour;
    }

    public Transport pricePerHour(BigDecimal pricePerHour) {
        this.setPricePerHour(pricePerHour);
        return this;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public BigDecimal getPricePerKm() {
        return this.pricePerKm;
    }

    public Transport pricePerKm(BigDecimal pricePerKm) {
        this.setPricePerKm(pricePerKm);
        return this;
    }

    public void setPricePerKm(BigDecimal pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public BigDecimal getCostPerHour() {
        return this.costPerHour;
    }

    public Transport costPerHour(BigDecimal costPerHour) {
        this.setCostPerHour(costPerHour);
        return this;
    }

    public void setCostPerHour(BigDecimal costPerHour) {
        this.costPerHour = costPerHour;
    }

    public BigDecimal getCostPerKm() {
        return this.costPerKm;
    }

    public Transport costPerKm(BigDecimal costPerKm) {
        this.setCostPerKm(costPerKm);
        return this;
    }

    public void setCostPerKm(BigDecimal costPerKm) {
        this.costPerKm = costPerKm;
    }

    public Boolean getIncludesDriver() {
        return this.includesDriver;
    }

    public Transport includesDriver(Boolean includesDriver) {
        this.setIncludesDriver(includesDriver);
        return this;
    }

    public void setIncludesDriver(Boolean includesDriver) {
        this.includesDriver = includesDriver;
    }

    public BigDecimal getDriverCost() {
        return this.driverCost;
    }

    public Transport driverCost(BigDecimal driverCost) {
        this.setDriverCost(driverCost);
        return this;
    }

    public void setDriverCost(BigDecimal driverCost) {
        this.driverCost = driverCost;
    }

    public String getFuelType() {
        return this.fuelType;
    }

    public Transport fuelType(String fuelType) {
        this.setFuelType(fuelType);
        return this;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public Transport contactInfo(String contactInfo) {
        this.setContactInfo(contactInfo);
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Transport isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return this.isTemplate;
    }

    public Transport isTemplate(Boolean isTemplate) {
        this.setIsTemplate(isTemplate);
        return this;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Transport createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Transport updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transport)) {
            return false;
        }
        return getId() != null && getId().equals(((Transport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transport{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", capacity=" + getCapacity() +
            ", pricePerHour=" + getPricePerHour() +
            ", pricePerKm=" + getPricePerKm() +
            ", costPerHour=" + getCostPerHour() +
            ", costPerKm=" + getCostPerKm() +
            ", includesDriver='" + getIncludesDriver() + "'" +
            ", driverCost=" + getDriverCost() +
            ", fuelType='" + getFuelType() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isTemplate='" + getIsTemplate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
