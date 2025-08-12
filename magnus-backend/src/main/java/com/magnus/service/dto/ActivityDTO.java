package com.magnus.service.dto;

import com.magnus.domain.enumeration.ActivityCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Activity} entity.
 */
@Schema(description = "Activity catalog and templates")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    private ActivityCategory category;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0")
    private BigDecimal baseCost;

    @NotNull
    @Min(value = 1)
    private Integer duration;

    @NotNull
    @Min(value = 1)
    private Integer maxCapacity;

    @NotNull
    @Size(max = 200)
    private String location;

    @NotNull
    private Boolean transportRequired;

    @NotNull
    private Boolean transportIncluded;

    @Lob
    private String equipmentProvided;

    @Lob
    private String requirements;

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

    public ActivityCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getTransportRequired() {
        return transportRequired;
    }

    public void setTransportRequired(Boolean transportRequired) {
        this.transportRequired = transportRequired;
    }

    public Boolean getTransportIncluded() {
        return transportIncluded;
    }

    public void setTransportIncluded(Boolean transportIncluded) {
        this.transportIncluded = transportIncluded;
    }

    public String getEquipmentProvided() {
        return equipmentProvided;
    }

    public void setEquipmentProvided(String equipmentProvided) {
        this.equipmentProvided = equipmentProvided;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
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
        if (!(o instanceof ActivityDTO)) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", basePrice=" + getBasePrice() +
            ", baseCost=" + getBaseCost() +
            ", duration=" + getDuration() +
            ", maxCapacity=" + getMaxCapacity() +
            ", location='" + getLocation() + "'" +
            ", transportRequired='" + getTransportRequired() + "'" +
            ", transportIncluded='" + getTransportIncluded() + "'" +
            ", equipmentProvided='" + getEquipmentProvided() + "'" +
            ", requirements='" + getRequirements() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isTemplate='" + getIsTemplate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
