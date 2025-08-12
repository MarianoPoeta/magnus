package com.magnus.service.dto;

import com.magnus.domain.enumeration.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Transport} entity.
 */
@Schema(description = "Transport catalog and templates")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransportDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    @Min(value = 1)
    private Integer capacity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal pricePerHour;

    @DecimalMin(value = "0")
    private BigDecimal pricePerKm;

    @DecimalMin(value = "0")
    private BigDecimal costPerHour;

    @DecimalMin(value = "0")
    private BigDecimal costPerKm;

    @NotNull
    private Boolean includesDriver;

    @DecimalMin(value = "0")
    private BigDecimal driverCost;

    @Size(max = 50)
    private String fuelType;

    @Lob
    private String contactInfo;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isTemplate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public BigDecimal getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(BigDecimal pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public BigDecimal getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(BigDecimal costPerHour) {
        this.costPerHour = costPerHour;
    }

    public BigDecimal getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(BigDecimal costPerKm) {
        this.costPerKm = costPerKm;
    }

    public Boolean getIncludesDriver() {
        return includesDriver;
    }

    public void setIncludesDriver(Boolean includesDriver) {
        this.includesDriver = includesDriver;
    }

    public BigDecimal getDriverCost() {
        return driverCost;
    }

    public void setDriverCost(BigDecimal driverCost) {
        this.driverCost = driverCost;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransportDTO)) {
            return false;
        }

        TransportDTO transportDTO = (TransportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportDTO{" +
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
